#!/usr/bin/env python3
"""
End-to-end test of the fuzzer, driven exactly the way tool/postprocessing_pipeline.py drives it.

    python3 tests/e2e/test_pipeline_e2e.py          (from the Differential-Fuzz-Testing folder)

It runs the same two commands the pipeline runs, with the same flags:

    1. scripts/project_setup.py <project> --original <tree> --refactored <tree>
    2. run.py <project> --original <tree> --refactored <tree> --report <path.md> --duration <d>

on a tiny project in tests/e2e/fixture/ whose correct results are known in advance:

    Calc.add      a + b   ->  b + a            EQUIVALENT
    Calc.half     n / 2   ->  n >> 1           DIVERGENT   (differs for negative odd n)
    Shape.area    abstract class; only reachable through its unchanged subclass Square
                                               EQUIVALENT
    Broken.value  refactored version does not compile
                                               not fuzzed; REFACTORING_BROKEN in compile_status.csv

It takes a few minutes (EvoSuite plus a short fuzz per method). Do NOT run it while the real
pipeline is running: both compile into the same target/ folder.

Everything it creates is removed at the end, including its profile in pom.xml and its entry in
projects.json.
"""
import csv
import os
import shutil
import subprocess
import sys
import tempfile
import unittest

MODULE = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", ".."))
FIXTURE = os.path.join(MODULE, "tests", "e2e", "fixture")
ORIGINAL = os.path.join(FIXTURE, "original")
REFACTORED = os.path.join(FIXTURE, "refactored")

PROJECT = "e2e-fuzz"
DURATION = "10s"  # the pipeline uses 120s; 10s is plenty for these methods


def run(*argv):
    """Run a command from the module folder; fail the test with its output if it fails."""
    r = subprocess.run([sys.executable, *argv], cwd=MODULE, capture_output=True, text=True)
    if r.returncode != 0:
        raise AssertionError(f"command failed: {' '.join(argv)}\n{r.stdout[-3000:]}\n{r.stderr[-3000:]}")
    return r.stdout


def read_csv(path):
    with open(path, newline="", encoding="utf-8") as f:
        return list(csv.DictReader(f))


def generated_paths():
    """Everything the pipeline writes for this project, so the test can clean up after itself."""
    return [os.path.join(MODULE, p) for p in (
        f"src/test/resources/{PROJECT}", f"src/test/fuzzing/{PROJECT}", f"target/sides/{PROJECT}",
        f"target/evosuite/{PROJECT}", f"target/seeds/{PROJECT}", f"target/fuzz-logs/{PROJECT}",
        f"target/cp-{PROJECT}.txt", f"reports/{PROJECT}", f"tools/fatjars/{PROJECT}.jar",
        f"tool/original-methods/{PROJECT}", f"tool/refactored-methods/{PROJECT}",
        os.path.join(ORIGINAL, "_javac_build"))]


def clean_up():
    subprocess.run([sys.executable, "scripts/project_setup.py", PROJECT, "--remove"],
                   cwd=MODULE, capture_output=True)
    for path in generated_paths():
        if os.path.isdir(path):
            shutil.rmtree(path)
        elif os.path.lexists(path):
            os.remove(path)


class FuzzerPipelineTest(unittest.TestCase):
    """The pipeline runs once for the whole class; each test checks one thing about its output."""

    @classmethod
    def setUpClass(cls):
        clean_up()  # start from nothing, even if an earlier run was interrupted
        cls.results_dir = tempfile.mkdtemp(prefix="e2e-fuzz-results-")
        report_md = os.path.join(cls.results_dir, f"{PROJECT}-{DURATION}.md")

        # Step 1 and 2: exactly the calls in tool/postprocessing_pipeline.py.
        run("scripts/project_setup.py", PROJECT, "--original", ORIGINAL, "--refactored", REFACTORED)
        run("run.py", PROJECT, "--original", ORIGINAL, "--refactored", REFACTORED,
            "--report", report_md, "--duration", DURATION)

        # The pipeline reads this CSV next to the .md report (read_fuzzer_output).
        cls.results = {row["Method"]: row for row in read_csv(report_md[:-len(".md")] + ".csv")}
        cls.compile_status = {row["File"]: row for row in
                              read_csv(os.path.join(MODULE, "reports", PROJECT, "compile_status.csv"))}

    @classmethod
    def tearDownClass(cls):
        clean_up()
        shutil.rmtree(cls.results_dir, ignore_errors=True)

    # ── project_setup.py ────────────────────────────────────────────────────────

    def test_setup_registered_the_project(self):
        with open(os.path.join(MODULE, "pom.xml"), encoding="utf-8") as f:
            pom = f.read()
        self.assertIn(f"<id>{PROJECT}</id>", pom, "no Maven profile for the project in pom.xml")
        self.assertTrue(os.path.isfile(os.path.join(MODULE, "tools", "fatjars", f"{PROJECT}.jar")),
                        "the project's fat jar was not built")

    # ── the results CSV the pipeline reads ──────────────────────────────────────

    def test_results_have_the_columns_the_pipeline_uses(self):
        row = next(iter(self.results.values()))
        for column in ("Method", "Signature", "Verdict", "Branch orig", "Branch ref",
                       "Line orig", "Line ref"):
            self.assertIn(column, row)

    def test_equivalent_refactoring(self):
        self.assertEqual(self.results["Calc.add"]["Verdict"], "EQUIVALENT")

    def test_divergent_refactoring(self):
        self.assertEqual(self.results["Calc.half"]["Verdict"], "DIVERGENT")

    def test_method_on_abstract_class_is_fuzzed_through_its_subclass(self):
        row = self.results["Shape.area"]
        self.assertEqual(row["Verdict"], "EQUIVALENT")
        self.assertGreater(int(row["Compared"]), 0, "no input was compared on both sides")

    def test_equivalent_verdicts_come_with_coverage(self):
        for method in ("Calc.add", "Shape.area"):
            covered = int(self.results[method]["Line orig"].split("/")[0])
            self.assertGreater(covered, 0, f"{method}: EQUIVALENT but no line of it ran")

    # ── refactorings that do not compile ────────────────────────────────────────

    def test_broken_refactoring_is_reported_and_not_fuzzed(self):
        self.assertEqual(self.compile_status["e2e/Broken.java"]["Status"], "REFACTORING_BROKEN")
        self.assertNotIn("Broken.value", self.results)

    def test_unchanged_class_is_not_fuzzed(self):
        self.assertNotIn("e2e/Square.java", self.compile_status)
        self.assertFalse(any(m.startswith("Square.") for m in self.results))

    # ── the snippet files the pipeline copies to tool/original-methods ──────────

    def test_every_result_has_its_snippet_files(self):
        # The RC judge and the final merge find a method's snippet by "<Class>.<Signature>.java",
        # built from the Method and Signature columns of the results CSV. So that name must exist.
        for method, row in self.results.items():
            name = method.split(".")[0] + "." + row["Signature"] + ".java"
            for side in ("original-methods", "refactored-methods"):
                snippet = os.path.join(MODULE, "tool", side, PROJECT, name)
                self.assertTrue(os.path.isfile(snippet), f"missing {side}/{PROJECT}/{name}")


if __name__ == "__main__":
    unittest.main(verbosity=2)
