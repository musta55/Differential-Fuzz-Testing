#!/usr/bin/env python3
"""
Turn an EvoSuite suite into candidate fuzzer seeds: the concrete values each test case uses.

    extract_seeds.py <project> [--side original]

Reads target/evosuite/<project>/evosuite-tests/**/<Class>_ESTest.java and writes
target/evosuite/<project>/seed-values.json:

    {"<manifest id>": [{"test": "test0", "values": [{"type": "int", "value": -2816}, ...]}, ...]}

WHAT IS EXTRACTED, AND WHY THAT SHAPE
The consumer (fuzz.auto.SeedWriter) does not replay a test case; it re-runs the engine's own
argument-building code against a recording provider, feeding it these values in order. So what it
needs is not a resolved call tree but an *ordered pool of typed constants per test case* — the
values EvoSuite found interesting for that path, in the order they appear. SeedWriter tries several
ways of aligning that pool with the receiver and the parameters, and keeps only alignments that
verifiably decode back to the intended arguments, so a mis-association here costs a seed, never
correctness.

That is why this is a literal scanner rather than a Java parser. EvoSuite emits calls like
`Integer.getInteger("", 2419)` and `new Foo(bar0)` whose values only exist at runtime; resolving
them properly would mean interpreting Java, while their *literals* are exactly the constants worth
seeding. A test case is attributed to a manifest method when its body mentions that method's name.
"""
import argparse
import json
import os
import re
import sys

MODULE = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))

# Order matters: string/char before the numeric patterns, so a literal like "12" is not mined
# for the number inside it. Longs/floats/doubles before int, so the suffix is not left behind.
LITERAL = re.compile(
    r'"(?P<str>(?:\\.|[^"\\])*)"'
    r"|'(?P<char>(?:\\.|[^'\\]))'"
    r"|\b(?P<hex>0[xX][0-9a-fA-F]+)[lL]?\b"
    r"|(?P<dbl>-?\b\d+\.\d+(?:[eE][-+]?\d+)?)[dDfF]?\b"
    r"|(?P<lng>-?\b\d+)[lL]\b"
    r"|(?P<int>-?\b\d+)\b"
    r"|\b(?P<bool>true|false)\b"
    r"|\b(?P<null>null)\b"
)

TEST_START = re.compile(r"public\s+void\s+(test\d+)\s*\(")


def unescape(s):
    return (s.replace(r"\\", "\x00").replace(r"\"", '"').replace(r"\'", "'")
             .replace(r"\n", "\n").replace(r"\r", "\r").replace(r"\t", "\t")
             .replace("\x00", "\\"))


def literals(body):
    """Ordered typed constants in one test body, minus the JUnit noise."""
    # assertEquals/verifyException carry the *expected output*, not an input, and seeding on a
    # return value pollutes the pool with values no parameter can take.
    body = re.sub(r"\b(assert\w*|verifyException|fail)\s*\([^;]*;", "", body)
    out = []
    for m in LITERAL.finditer(body):
        if m.group("str") is not None:
            out.append({"type": "String", "value": unescape(m.group("str"))})
        elif m.group("char") is not None:
            out.append({"type": "char", "value": unescape(m.group("char"))})
        elif m.group("hex") is not None:
            out.append({"type": "long", "value": int(m.group("hex"), 16)})
        elif m.group("dbl") is not None:
            out.append({"type": "double", "value": float(m.group("dbl"))})
        elif m.group("lng") is not None:
            out.append({"type": "long", "value": int(m.group("lng"))})
        elif m.group("int") is not None:
            out.append({"type": "int", "value": int(m.group("int"))})
        elif m.group("bool") is not None:
            out.append({"type": "boolean", "value": m.group("bool") == "true"})
        elif m.group("null") is not None:
            out.append({"type": "null", "value": None})
    return out


def test_bodies(src):
    """Split a suite into (name, body) pairs by brace-matching each @Test method."""
    out = []
    for m in TEST_START.finditer(src):
        brace = src.find("{", m.end())
        if brace < 0:
            continue
        depth, i = 0, brace
        while i < len(src):
            if src[i] == "{":
                depth += 1
            elif src[i] == "}":
                depth -= 1
                if depth == 0:
                    break
            i += 1
        out.append((m.group(1), src[brace + 1:i]))
    return out


def main():
    ap = argparse.ArgumentParser(description=__doc__,
                                 formatter_class=argparse.RawDescriptionHelpFormatter)
    ap.add_argument("project")
    ap.add_argument("--side", default="original", choices=("original", "refactored"))
    args = ap.parse_args()

    base = os.path.join(MODULE, "target/evosuite", args.project)
    man = json.load(open(os.path.join(MODULE, "src/test/resources", args.project, "manifest.json")))

    # One suite per class; each manifest method looks for its own name inside that suite's tests.
    by_class = {}
    for e in man["methods"]:
        by_class.setdefault(e[args.side], []).append(e)

    seeds, stats = {}, {"suites": 0, "missing": 0, "methods": 0, "cases": 0}
    for fqn, entries in by_class.items():
        suite = os.path.join(base, "evosuite-tests", fqn.replace(".", "/") + "_ESTest.java")
        if not os.path.isfile(suite):
            stats["missing"] += 1
            continue
        stats["suites"] += 1
        src = open(suite, encoding="utf-8", errors="replace").read()
        bodies = test_bodies(src)
        for e in entries:
            # A constructor entry is exercised by every test (they all have to build the object);
            # a named method only by the tests that call it.
            name = e["method"]
            cases = []
            for tname, body in bodies:
                if name != "<init>" and not re.search(r"\b" + re.escape(name) + r"\s*\(", body):
                    continue
                vals = literals(body)
                if vals:
                    cases.append({"test": tname, "values": vals})
            if cases:
                seeds[e["id"]] = cases
                stats["methods"] += 1
                stats["cases"] += len(cases)

    out = os.path.join(base, "seed-values.json")
    os.makedirs(base, exist_ok=True)
    with open(out, "w") as f:
        json.dump(seeds, f, indent=1)
    print(f"{args.project}: {stats['suites']} suites ({stats['missing']} missing), "
          f"{stats['cases']} test cases -> {stats['methods']}/{len(man['methods'])} methods")
    print(f"  -> {out}")
    return 0


if __name__ == "__main__":
    sys.exit(main())
