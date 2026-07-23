#!/usr/bin/env python3
"""
Generate one thin Jazzer harness per method from a project's manifest.json.

Each harness is namespaced in package fuzz.auto.<projkey> (so corpora/reports stay
per-project) and just calls GenericDifferential.run(data, project, id) — the spec lives in
the manifest, not in the harness.

Usage: gen_harnesses.py <project> [maxDuration=1m]
"""
import json
import os
import sys

MODULE = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))

TEMPLATE = '''package fuzz.auto.{projkey};

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.junit.FuzzTest;

import fuzz.auto.GenericDifferential;

/** AUTO-GENERATED from src/test/resources/{project}/manifest.json — fuzzes {id}. */
public class {harness}
{{
  @FuzzTest(maxDuration = "{dur}")
  public void differential(FuzzedDataProvider data) throws Throwable
  {{
    GenericDifferential.run(data, "{project}", "{id}");
  }}
}}
'''


def main(project, dur):
    projkey = project.replace("-", "_")
    man = json.load(open(os.path.join(MODULE, "src/test/resources", project, "manifest.json")))
    # harnesses live in a SEPARATE tree from the project source (Dataset/), per-project
    outdir = os.path.join(MODULE, "src/test/fuzzing", project, "fuzz/auto", projkey)
    if os.path.isdir(outdir):
        for f in os.listdir(outdir):
            if f.endswith("FuzzTest.java"):
                os.remove(os.path.join(outdir, f))
    os.makedirs(outdir, exist_ok=True)
    for e in man["methods"]:
        harness = "Auto_" + e["id"].replace(".", "_") + "_FuzzTest"
        open(os.path.join(outdir, harness + ".java"), "w").write(
            TEMPLATE.format(projkey=projkey, project=project, id=e["id"], harness=harness, dur=dur))
    print(f"generated {len(man['methods'])} harnesses in fuzz.auto.{projkey} (maxDuration={dur})")


if __name__ == "__main__":
    if len(sys.argv) < 2:
        print(__doc__)
        sys.exit(2)
    main(sys.argv[1], sys.argv[2] if len(sys.argv) > 2 else "1m")
