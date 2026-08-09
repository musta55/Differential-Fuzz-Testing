#!/usr/bin/env python3
"""
Fuzz every changed method of one project and report whether the refactoring preserved behaviour.

    run_project.py <project> [--max N] [--regression] [--report NAME] [--report-dir DIR]
                             [--keep-corpus]

For each method in the manifest: run its Jazzer harness (JAZZER_FUZZ=1, seeded from the EvoSuite
corpus staged in target/seeds/<project>), classify the outcome, measure per-method coverage on BOTH
snapshots, and write reports/<project>/auto-fuzz-report.md.

COVERAGE IS DIFFERENTIAL, AND COMES FROM JAZZER
Every input runs through the original and the refactored method in the same iteration, so both sides
have coverage and the report shows both. That pairing is what makes an EQUIVALENT verdict readable:
"no divergence" is only as strong as the fraction of each version's branches the fuzzer actually
reached, and a one-sided number would hide the case where the refactoring added a branch nothing
ever exercised.

The numbers are Jazzer's own. It instruments what it loads in order to steer its search, so its
coverage is exactly the fuzzer's reach — nothing is counted that the fuzzer did not drive.
fuzz.auto.JazzerCoverage dumps it at JVM exit in JaCoCo .exec format, which scripts/CovReport reads
with the jacoco-core LIBRARY. There is no JaCoCo agent anywhere in this pipeline; two agents
instrumenting the same classes made Jazzer record jacoco-rewritten bytes whose class ids no longer
matched the class files, and every method reported 0/0.

A one-sided watchdog TIMEOUT is treated as inconclusive by the engine (not divergent).
"""
import glob
import json
import os
import re
import shutil
import subprocess
import sys
import time

MODULE = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))

SEED_STAGING = "target/seeds"        # master corpus, written by fuzz.auto.SeedWriter
SEED_INSTALL = "src/test/resources"  # where jazzer-junit looks, via the test classpath

JAZZER_EXEC = "target/jazzer-cov.exec"


def cov_classpath():
    """Classpath for scripts/CovReport: the jacoco-core library (a reader, not an agent) + asm."""
    core = glob.glob(os.path.expanduser(
        "~/.m2/repository/org/jacoco/org.jacoco.core/0.8.11/org.jacoco.core-0.8.11.jar"))
    asm = os.path.expanduser("~/.m2/repository/org/ow2/asm")
    jars = [f"{MODULE}/target/covtool"]
    if core:
        jars.append(core[0])
    for a in ("asm/9.6/asm-9.6.jar", "asm-commons/9.6/asm-commons-9.6.jar",
              "asm-tree/9.6/asm-tree-9.6.jar", "asm-analysis/9.6/asm-analysis-9.6.jar"):
        jars.append(os.path.join(asm, a))
    return ":".join(jars)


def seed_dirs(project):
    projkey = project.replace("-", "_")
    rel = os.path.join("fuzz/auto", projkey)
    return (os.path.join(MODULE, SEED_STAGING, project, rel),
            os.path.join(MODULE, SEED_INSTALL, rel),
            # Jazzer reads seeds off the CLASSPATH, i.e. from the copy maven-resources-plugin made
            # in target/test-classes — and that plugin never deletes a resource that disappeared
            # from the source tree, so a stale corpus would survive a regeneration unnoticed.
            os.path.join(MODULE, "target/test-classes", rel))


def install_seeds(project):
    """Refresh the installed seed corpus from staging, and say how many seeds the run will use."""
    staging, install, compiled = seed_dirs(project)
    if os.path.isdir(install):
        shutil.rmtree(install)
    # Only the *Inputs trees under the compiled copy: the compiled harness .class files live in the
    # same directory and deleting them would force a needless recompile of every harness.
    if os.path.isdir(compiled):
        for name in os.listdir(compiled):
            if name.endswith("Inputs"):
                shutil.rmtree(os.path.join(compiled, name), ignore_errors=True)
    if not os.path.isdir(staging):
        print("  seeds: none staged — run scripts/gen_unittests.py then scripts/gen_seeds.py")
        return 0
    shutil.copytree(staging, install)
    n = sum(len(fs) for _, _, fs in os.walk(install))
    print(f"  seeds: {n} files from {os.path.relpath(staging, MODULE)}")
    return n


def clear_generated_corpus():
    """Delete the corpus libFuzzer carries over between runs.

    Jazzer keeps the inputs it found interesting under target/fuzz-cwd/.cifuzz-corpus/ and reuses
    them next time, so a report would otherwise describe every run since the last clean rather than
    this one — and the seed corpus's contribution would be indistinguishable from what a previous
    run had already discovered. --keep-corpus opts into the compounding behaviour deliberately.
    """
    corpus = os.path.join(MODULE, "target/fuzz-cwd/.cifuzz-corpus")
    if os.path.isdir(corpus):
        n = len(os.listdir(corpus))
        shutil.rmtree(corpus)
        print(f"  corpus: cleared {n} carried-over directories")


def harness_duration(project):
    """Read the maxDuration the harnesses were generated with (for the report header)."""
    projkey = project.replace("-", "_")
    files = glob.glob(os.path.join(MODULE, "src/test/fuzzing", project,
                                   "fuzz/auto", projkey, "*FuzzTest.java"))
    if files:
        mm = re.search(r'maxDuration\s*=\s*"([^"]+)"', open(files[0]).read())
        if mm:
            return mm.group(1)
    return "?"


def constructible(entry, project):
    """Whether it is worth launching a fuzz run for this entry at all.

    Deliberately almost always True. An older version pre-skipped, by source regex, every instance
    method on an abstract class or one with no public constructor — precisely the set the engine can
    now handle via autofuzz and concrete-subtype substitution. The only remaining pre-skip is a
    missing snapshot, which means the pair was pruned and there is nothing on the classpath to
    invoke. Everything else runs and the engine decides at runtime, printing [SKIP] with a reason.
    """
    if entry["static"]:
        return True
    path = os.path.join(MODULE, "src/test/Dataset", project,
                        entry["refactored"].replace(".", "/") + ".java")
    return os.path.isfile(path)


def coverage(cp, simple_name, method):
    """(branch, line) for one method of one snapshot class, from this run's Jazzer dump."""
    exe = os.path.join(MODULE, JAZZER_EXEC)
    if not os.path.exists(exe):
        return "n/a", "n/a"
    p = subprocess.run(["java", "-cp", cp, "CovReport", exe,
                        f"{MODULE}/target/test-classes", simple_name, method],
                       capture_output=True, text=True)
    # CovReport filters classes by substring, so "WidgetOriginal" also matches nothing else, but a
    # bare "Widget" would match both snapshots; the caller always passes the suffixed name.
    line = p.stdout.strip().splitlines()[0] if p.stdout.strip() else ""
    b = re.search(r"branch=(\d+/\d+)", line)
    l = re.search(r"line=(\d+/\d+)", line)
    return (b.group(1) if b else "n/a"), (l.group(1) if l else "n/a")


def covered(frac):
    """(covered, total) from a 'c/t' cell, or None when there is no number."""
    if not frac or "/" not in frac:
        return None
    try:
        a, b = frac.split("/", 1)
        return int(a), int(b)
    except ValueError:
        return None


def confidence(branch_o, branch_r):
    """How much an EQUIVALENT verdict is worth, stated in terms of what was actually exercised.

    An EQUIVALENT is never a proof — it means "no counterexample inside the budget" — so the report
    has to say how hard the fuzzer looked. Branch coverage is that measure, taken over both sides
    because a refactoring can add branches to one of them.
    """
    fo, fr = covered(branch_o), covered(branch_r)
    tot = sum(f[1] for f in (fo, fr) if f)
    cov = sum(f[0] for f in (fo, fr) if f)
    if tot == 0:
        return "branchless (judge on Line)"
    pctv = 100.0 * cov / tot
    if pctv >= 99:
        return f"all {tot} branches exercised"
    if pctv >= 60:
        return f"{cov}/{tot} branches ({pctv:.0f}%)"
    return f"only {cov}/{tot} branches ({pctv:.0f}%) — weak"


def main(project, max_n, mode, report_name, keep_corpus, report_dir=None):
    projkey = project.replace("-", "_")
    man = json.load(open(os.path.join(MODULE, "src/test/resources", project, "manifest.json")))
    entries = man["methods"][:max_n] if max_n else man["methods"]
    dur = harness_duration(project)
    cp = cov_classpath()
    os.makedirs(f"{MODULE}/target/covtool", exist_ok=True)
    subprocess.run(["javac", "-cp", cp.split(":")[1] if ":" in cp else cp,
                    "-d", f"{MODULE}/target/covtool", f"{MODULE}/scripts/CovReport.java"],
                   capture_output=True)

    if not keep_corpus:
        clear_generated_corpus()
    n_seeds = install_seeds(project)

    logdir = os.path.join(MODULE, "target/fuzz-logs", project)
    os.makedirs(logdir, exist_ok=True)
    # --report-dir lets several unrelated runs share the `example` profile without overwriting each
    # other's reports, which is what scripts/check_issues.py needs: every case is built into the
    # same profile directories, so without this each case's report replaced the previous one.
    repdir = report_dir if report_dir else os.path.join(MODULE, "reports", project)
    if not os.path.isabs(repdir):
        repdir = os.path.join(MODULE, repdir)
    os.makedirs(repdir, exist_ok=True)
    report = os.path.join(repdir, report_name)

    env = dict(os.environ)
    if mode == "fuzz":
        env["JAZZER_FUZZ"] = "1"
    else:
        env.pop("JAZZER_FUZZ", None)

    classes = sorted({e["original"].rsplit(".", 1)[0] + "." + e["source"]["class"]
                      if "source" in e else e["original"] for e in entries})
    rows = []
    # Three verdicts only. Everything that is not a two-sided comparison with a definite answer is
    # SKIP, whatever the mechanism — the sub-reason is kept per row and totalled separately, so the
    # top line stays readable without discarding why a method could not be answered.
    counts = {k: 0 for k in ("EQUIVALENT", "DIVERGENT", "SKIP")}
    skip_reasons = {}

    for i, e in enumerate(entries, 1):
        harness = f"fuzz.auto.{projkey}.Auto_" + e["id"].replace(".", "_") + "_FuzzTest"
        log = os.path.join(logdir, e["id"].replace(".", "_") + ".log")
        osimple = e["original"].split(".")[-1]
        rsimple = e["refactored"].split(".")[-1]
        print(f"  [{i}/{len(entries)}] {e['id']} ({e.get('kind','?')}) ...", end=" ", flush=True)

        if not constructible(e, project):
            counts["SKIP"] += 1
            skip_reasons["pruned snapshot"] = skip_reasons.get("pruned snapshot", 0) + 1
            print("SKIP (snapshot pruned)")
            rows.append({"id": e["id"], "kind": e.get("kind", "?"), "result": "SKIP",
                         "reason": "pruned snapshot", "runs": "-", "fails": 0,
                         "inputs": 0, "cmp": 0,
                         "bo": "n/a", "br": "n/a", "lo": "n/a", "lr": "n/a",
                         "why": "snapshot pruned by the compile gate, not on the classpath",
                         "conf": "-"})
            continue

        exe = os.path.join(MODULE, JAZZER_EXEC)
        if os.path.exists(exe):
            os.remove(exe)
        argv = ["timeout", "240", "./mvnw", f"-P{project}", "test", f"-Dtest={harness}",
                "-Dsurefire.failIfNoSpecifiedTests=false", "-Dmaven.test.failure.ignore=true",
                f"-Dfuzz.jvmArgs=-Dfuzz.jazzerCoverage={exe}"]
        with open(log, "w") as lf:
            subprocess.run(argv, cwd=MODULE, env=env, stdout=lf, stderr=subprocess.STDOUT)
        out = open(log, errors="replace").read()

        m = re.search(r"Tests run: (\d+), Failures: (\d+), Errors: (\d+)", out)
        runs = m.group(1) if m else "?"
        fe = (int(m.group(2)) + int(m.group(3))) if m else 0
        # How much testing the budget actually bought. Surefire's "Tests run" counts JUnit
        # invocations (each seed once, plus one call for the whole fuzzing session), not fuzz
        # iterations, so the engine counts them itself and prints them at JVM exit.
        st = re.search(r"\[DIFF-STATS\] inputs=(\d+) comparisons=(\d+)", out)
        n_inputs = int(st.group(1)) if st else 0
        n_cmp = int(st.group(2)) if st else 0
        bo, lo = coverage(cp, osimple, e["method"])
        br, lr = coverage(cp, rsimple, e["method"])

        if "DIFFERENTIAL MISMATCH" in out:
            res = "DIVERGENT"
            reason = re.search(r"reason    : (.+)", out)
            om = re.search(r"original  : (.+)", out)
            rm = re.search(r"refactored: (.+)", out)
            args = re.search(r"methodArgs: (.+)", out)
            why = (f"**{reason.group(1).strip()}** — on `{args.group(1).strip()[:44]}` "
                   f"original {om.group(1).strip()[:34]}, refactored {rm.group(1).strip()[:34]}"
                   if reason and om and rm else "mismatch (see log)")
            conf = "witnessed"
            reason = "-"
        elif "[SKIP]" in out:
            sm = re.search(r"\[SKIP\].*?— (.+)", out)
            res, reason = "SKIP", "structurally untestable"
            why = sm.group(1).strip()[:80] if sm else "receiver not constructible"
            conf = "-"
        elif "com.code_intelligence.jazzer.api.FuzzerSecurityIssue" in out:
            # A Jazzer sanitizer fired (reflective call / SSRF / ReDoS ...). The method DID run on
            # both sides and did not diverge — a finding about the code, not a harness failure.
            sm = sorted(set(re.findall(r"FuzzerSecurityIssue(Critical|High|Medium|Low)", out)))
            tm = re.search(r"FuzzerSecurityIssue\w+:\s*\n(.+)", out)
            res, reason = "SKIP", "sanitizer finding"
            why = ("ran on both sides without diverging, but a Jazzer sanitizer fired on the code "
                   f"itself ({'/'.join(sm) or '?'}): {tm.group(1).strip()[:50] if tm else 'see log'}")
            conf = confidence(bo, br)
        elif m and fe == 0 and "[DIFF-RAN]" not in out:
            # The harness "passed" without ever invoking both sides: every iteration bailed because
            # a receiver or argument could not be built. Reporting that as EQUIVALENT would be a
            # false negative dressed up as a result, so it gets its own verdict.
            res, reason = "SKIP", "never ran"
            # The engine names the side that refused to be built; without it "never ran" reads the
            # same whether a parameter type was unsynthesisable or the refactored constructor now
            # throws — and the second is a real finding about the refactoring.
            um = re.findall(r"\[UNBUILT\] (.+)", out)
            detail = ("; ".join(dict.fromkeys(x.strip() for x in um))[:110] if um
                      else "no [UNBUILT] diagnostic in the log")
            why = f"never built on both sides — {detail} (NOT equivalence)"
            conf = "-"
        elif m and fe == 0:
            res, reason = "EQUIVALENT", "-"
            why = f"no divergence found in {dur} of fuzzing"
            conf = confidence(bo, br)
        else:
            res, reason = "SKIP", "harness error"
            em = re.search(
                r"(NoSuchMethodException|NoClassDefFoundError|ClassNotFoundException|"
                r"InstantiationException)", out)
            why = (em.group(1) if em else "harness error") + " — could not be tested"
            conf = "-"

        counts[res] += 1
        if res == "SKIP":
            skip_reasons[reason] = skip_reasons.get(reason, 0) + 1
        print(f"{res}{'' if reason == '-' else ' (' + reason + ')'}"
              f"  ({n_cmp} comparisons, branch orig {bo} / ref {br})")
        rows.append({"id": e["id"], "kind": e.get("kind", "?"), "result": res, "reason": reason,
                     "runs": runs, "fails": fe, "inputs": n_inputs, "cmp": n_cmp,
                     "bo": bo, "br": br, "lo": lo, "lr": lr, "why": why, "conf": conf})

    write_report(report, project, man, entries, classes, rows, counts, skip_reasons, dur, mode,
                 n_seeds, keep_corpus)
    print(f"Done. EQUIVALENT={counts['EQUIVALENT']} DIVERGENT={counts['DIVERGENT']} "
          f"SKIP={counts['SKIP']}  ->  {report}")


def write_report(path, project, man, entries, classes, rows, counts, skip_reasons, dur, mode,
                 n_seeds, keep_corpus):
    tested = counts["EQUIVALENT"] + counts["DIVERGENT"]
    with open(path, "w") as r:
        r.write(f"# Differential-Fuzzing Report — {project}\n\n")
        r.write(f"Generated {time.strftime('%Y-%m-%d %H:%M:%S')} · mode **{mode}** · "
                f"**{dur}** per method · seed corpus **{n_seeds}** files · "
                f"carried-over corpus **{'kept' if keep_corpus else 'cleared'}**\n\n")
        src = man.get("source", {})
        if src:
            r.write(f"- original tree: `{src.get('original','?')}`\n")
            r.write(f"- refactored tree: `{src.get('refactored','?')}`\n\n")

        r.write("## Summary\n\n")
        r.write(f"**{len(classes)} classes · {len(entries)} changed methods**\n\n")
        r.write("| Outcome | Count | Meaning |\n|---|---:|---|\n")
        r.write(f"| **EQUIVALENT** | {counts['EQUIVALENT']} | no input made the two versions "
                "differ within the budget — trust it in proportion to the Confidence column |\n")
        r.write(f"| **DIVERGENT** | {counts['DIVERGENT']} | a concrete input makes them differ: "
                "a real behavioural change |\n")
        r.write(f"| **SKIP** | {counts['SKIP']} | no verdict could be reached — see the breakdown "
                "below |\n\n")
        r.write(f"**{tested} of {len(entries)} methods got a verdict**; {counts['SKIP']} could not "
                "be processed. Only EQUIVALENT and DIVERGENT are evidence about behaviour.\n\n")

        total_in = sum(x.get("inputs", 0) for x in rows)
        total_cmp = sum(x.get("cmp", 0) for x in rows)
        r.write(f"**Testing effort:** every method got a fixed budget of **{dur}**, in which the "
                f"fuzzer generated **{total_in:,} inputs** and completed "
                f"**{total_cmp:,} differential comparisons** (both versions invoked on the same "
                "input). The per-method counts are in the table below.\n\n")
        r.write("> `Inputs` is every input the fuzzer produced; `Compared` is only those that got "
                "as far as running BOTH versions. A large gap means most inputs died building a "
                "receiver or an argument, which is exactly when an EQUIVALENT should be doubted — "
                "and it is invisible in surefire's \"Tests run\", which counts JUnit invocations "
                "(each seed once, plus one call for the whole fuzzing session), not fuzz "
                "iterations.\n\n")

        if skip_reasons:
            # The three-bucket summary is the headline, but "SKIP" alone cannot be acted on: a
            # pruned snapshot is a build problem, a never-ran method is usually a fact about the
            # refactoring, and a sanitizer finding actually did execute on both sides.
            r.write("### Why the SKIPs\n\n| Reason | Count | What it means |\n|---|---:|---|\n")
            meanings = {
                "never ran": "the harness completed but no input ever built a receiver **and** "
                             "arguments for **both** sides, so nothing was ever compared. Usually a "
                             "fact about the refactoring — e.g. the refactored constructor throws. "
                             "This is **not** equivalence",
                "structurally untestable": "no input could ever work: an abstract receiver with no "
                                           "concrete subtype, or a parameter type nothing can build",
                "pruned snapshot": "the pair did not compile and was dropped by the compile gate",
                "harness error": "missing class at runtime, inaccessible member, or a bad manifest "
                                 "entry",
                "sanitizer finding": "it *did* run on both sides without diverging, but a Jazzer "
                                     "sanitizer fired on the code itself — worth reading, not a "
                                     "differential result",
            }
            for reason, n in sorted(skip_reasons.items(), key=lambda kv: -kv[1]):
                r.write(f"| {reason} | {n} | {meanings.get(reason, '')} |\n")
            r.write("\n")

        r.write("> Fuzzing is **sound for non-equivalence and incomplete for equivalence**: a "
                "DIVERGENT is a witnessed fact, an EQUIVALENT is only \"no counterexample found "
                "in the budget\". That is what the Confidence column is for — it reports how much "
                "of the two versions the fuzzer actually reached, so an EQUIVALENT at 0/8 branches "
                "can be told apart from one at 8/8.\n\n")

        r.write("## Per method\n\n")
        r.write("Coverage is **differential**: `orig` is the `<Class>Original` snapshot, `ref` the "
                "`<Class>Refactored` one, both measured by Jazzer in the same run.\n\n")
        r.write("| Method | Kind | Verdict | Reason | Inputs | Compared | Branch orig | "
                "Branch ref | Line orig | Line ref | Confidence | Why |\n")
        r.write("|---|---|---|---|---:|---:|---|---|---|---|---|---|\n")
        for x in rows:
            r.write(f"| `{x['id']}` | {x['kind']} | **{x['result']}** | {x.get('reason','-')} "
                    f"| {x.get('inputs', 0):,} | {x.get('cmp', 0):,} "
                    f"| {x['bo']} | {x['br']} | {x['lo']} | {x['lr']} | {x['conf']} | {x['why']} |\n")

        div = [x for x in rows if x["result"] == "DIVERGENT"]
        if div:
            r.write("\n## Divergences\n\n")
            for x in div:
                r.write(f"- **`{x['id']}`** — {x['why']}\n")
            r.write(f"\nReproduce with:\n```bash\ngrep -a -A6 -F '[DIFFERENTIAL MISMATCH]' "
                    f"target/fuzz-logs/{project}/<Class>_<method>.log\n```\n")

        weak = [x for x in rows if x["result"] == "EQUIVALENT" and "weak" in x["conf"]]
        if weak:
            r.write("\n## EQUIVALENT verdicts to distrust\n\n")
            r.write("These were never contradicted, but the fuzzer reached too little of the code "
                    "for that to mean much. Raise `--duration`, or improve the seed corpus.\n\n")
            for x in weak:
                r.write(f"- `{x['id']}` — {x['conf']}\n")

        r.write(f"\n<!-- config: seeds={n_seeds} duration={dur} mode={mode} "
                f"keepcorpus={keep_corpus} coverage=jazzer -->\n")


if __name__ == "__main__":
    if len(sys.argv) < 2:
        print(__doc__)
        sys.exit(2)
    a = sys.argv[1:]
    proj = a[0]
    mx = int(a[a.index("--max") + 1]) if "--max" in a else 0
    md = "regression" if "--regression" in a else "fuzz"
    rn = a[a.index("--report") + 1] if "--report" in a else "auto-fuzz-report.md"
    rd = a[a.index("--report-dir") + 1] if "--report-dir" in a else None
    main(proj, mx, md, rn, "--keep-corpus" in a, rd)
