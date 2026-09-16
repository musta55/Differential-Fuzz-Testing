#!/usr/bin/env python3
"""
Mine fuzzer seeds from the project's own source, for projects EvoSuite cannot reach.

    source_seeds.py <project> [--side original]

EvoSuite 1.2.0 runs only on a Java 8 JVM, so a project compiled for a newer release gets no
generated suites at all and therefore no seed corpus (openmeetings: class-file 61, 0 seeds, and the
fuzzer then spends its whole budget rediscovering constants that are written down in the code).
This script is the fallback: it reads the constants out of the snapshot source directly, so it
works on any JDK and needs nothing compiled.

It writes the SAME file, in the same shape, that scripts/extract_seeds.py writes:

    target/evosuite/<project>/seed-values.json
    {"<manifest id>": [{"test": "<label>", "values": [{"type": "int", "value": -2816}, ...]}, ...]}

so scripts/gen_seeds.py encodes it with fuzz.auto.SeedWriter exactly as it does EvoSuite's output,
and every downstream step is unchanged.

WHAT IT MINES, AND WHY THAT IS THE RIGHT SHAPE
SeedWriter does not replay anything; it re-runs the engine's own argument builder against a
recording provider, consuming an *ordered pool of typed constants per case*. So the useful unit is
"the literals this method's body mentions, in order" -- the boundary values, sentinel strings and
magic numbers the method actually branches on. Two pools are emitted per method:

    source:<method>#<n>   the literals in that method declaration's body
    source:class          the class's field initialisers and constants, which is where Java code
                          usually keeps the interesting strings

This is weaker evidence than EvoSuite's search-derived values -- these constants are not known to
reach any particular branch -- but it is drawn from the code under test, which is exactly where a
differential fuzzer's interesting inputs live. A mis-associated value costs a seed, never
correctness: SeedWriter verifies every seed by decoding it back.
"""
import argparse
import json
import os
import re
import sys

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from extract_seeds import typed_literals  # noqa: E402  (needs the path above)

MODULE = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))

# A method or constructor declaration: a name, a parameter list with no nested parens, an optional
# throws clause, then the opening brace. The parameter list deliberately excludes ( ) ; { } so that
# a call expression cannot be mistaken for a declaration.
DECL = re.compile(
    r"(?<![.\w])(?P<name>[A-Za-z_$][\w$]*)\s*\([^;{}()]*\)\s*"
    r"(?:throws\s+[\w.,\s<>\[\]]+?\s*)?\{"
)

# Control structures match DECL's shape exactly (`if (x) {`), so they have to be named out.
KEYWORDS = {"if", "for", "while", "switch", "catch", "synchronized", "try", "do", "else",
            "return", "new", "case", "assert", "throw", "super", "this"}

# Comments carry prose, dates and issue numbers that are not inputs to anything.
BLOCK_COMMENT = re.compile(r"/\*.*?\*/", re.S)
LINE_COMMENT = re.compile(r"//[^\n]*")


def strip_comments(src):
    """Blank out comments while preserving offsets, so brace matching still lines up."""
    def blank(m):
        return re.sub(r"\S", " ", m.group(0))
    return LINE_COMMENT.sub(blank, BLOCK_COMMENT.sub(blank, src))


def method_bodies(src):
    """[(name, body)] for every method and constructor declaration, outermost first.

    Nested declarations (anonymous classes, lambdas with blocks) fall inside an enclosing body and
    are therefore counted with it, which is what we want: their literals are still literals this
    method mentions.
    """
    out, i = [], 0
    while True:
        m = DECL.search(src, i)
        if not m:
            return out
        if m.group("name") in KEYWORDS:
            i = m.end()
            continue
        brace = m.end() - 1
        depth, j = 0, brace
        while j < len(src):
            if src[j] == "{":
                depth += 1
            elif src[j] == "}":
                depth -= 1
                if depth == 0:
                    break
            j += 1
        out.append((m.group("name"), src[brace + 1:j]))
        i = j + 1 if j < len(src) else m.end()


def class_level(src, bodies_spans):
    """Everything outside any method body: field initialisers, constants, enum entries."""
    keep, prev = [], 0
    for start, end in bodies_spans:
        keep.append(src[prev:start])
        prev = end
    keep.append(src[prev:])
    return "".join(keep)


def source_path(project, entry, side):
    """The snapshot the fuzzer actually targets, e.g. .../<pkg>/<Class>Original.java."""
    return os.path.join(MODULE, "src/test/Dataset", project,
                        entry[side].replace(".", "/") + ".java")


def main():
    ap = argparse.ArgumentParser(description=__doc__,
                                 formatter_class=argparse.RawDescriptionHelpFormatter)
    ap.add_argument("project")
    ap.add_argument("--side", default="original", choices=("original", "refactored"))
    args = ap.parse_args()

    base = os.path.join(MODULE, "target/evosuite", args.project)
    os.makedirs(base, exist_ok=True)
    man = json.load(open(os.path.join(MODULE, "src/test/resources", args.project,
                                      "manifest.json")))

    by_class = {}
    for e in man["methods"]:
        by_class.setdefault(e[args.side], []).append(e)

    seeds = {}
    stats = {"classes": 0, "missing": 0, "methods": 0, "cases": 0}
    for fqn, entries in by_class.items():
        path = source_path(args.project, entries[0], args.side)
        if not os.path.isfile(path):
            stats["missing"] += 1
            continue
        stats["classes"] += 1
        src = strip_comments(open(path, encoding="utf-8", errors="replace").read())

        bodies = method_bodies(src)
        spans = []
        for _, body in bodies:
            at = src.find(body)
            if at >= 0:
                spans.append((at, at + len(body)))
        shared = typed_literals(class_level(src, sorted(spans)))

        # The snapshot class is <Class>Original / <Class>Refactored, but its constructors are
        # declared under that same suffixed name, so a ctor entry looks for the simple class name.
        simple = fqn.rsplit(".", 1)[-1]
        for e in entries:
            want = simple if e["method"] == "<init>" else e["method"]
            cases = []
            for n, (name, body) in enumerate(bodies):
                if name != want:
                    continue
                vals = typed_literals(body)
                if vals:
                    cases.append({"test": f"source:{want}#{n}", "values": vals})
            if shared:
                cases.append({"test": "source:class", "values": shared})
            if cases:
                seeds[e["id"]] = cases
                stats["methods"] += 1
                stats["cases"] += len(cases)

    out = os.path.join(base, "seed-values.json")
    with open(out, "w") as f:
        json.dump(seeds, f, indent=1)
    print(f"mined {stats['cases']} constant pools for {stats['methods']} methods from "
          f"{stats['classes']} source files -> {os.path.relpath(out, MODULE)}")
    if stats["missing"]:
        print(f"  {stats['missing']} class(es) had no snapshot source on disk")
    return 0


if __name__ == "__main__":
    sys.exit(main())
