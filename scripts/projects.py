#!/usr/bin/env python3
"""
The project registry: one JSON file recording everything the pipeline needs to run a
project, so nothing about a target project is hard-coded in a script.

    projects.json          (repo root, git-ignored — the paths in it are machine-local)
    {
      "deltaspike": {
        "original":   "/abs/path/projects/before/deltaspike",
        "refactored": "/abs/path/projects/after/deltaspike",
        "projectDir": "/abs/path/projects/before/deltaspike",
        "jar":        "/abs/.../deltaspike-differential-fuzz-testing.jar",
        "java":       "8",
        "registered": "2026-09-15T12:00:00"
      }
    }

`scripts/project_setup.py` writes an entry; `run.py` and `scripts/run_tmux.sh` read it, so
after registering once a run is just `scripts/run_tmux.sh deltaspike`. Every consumer still
accepts explicit --original/--refactored, which override the registry.

The two bundled demos (example, apex-core) are pre-seeded on first read so they keep working
with no registration step.
"""
import json
import os
from datetime import datetime

MODULE = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))
REGISTRY = os.path.join(MODULE, "projects.json")

# Bundled trees that ship with the repo. Merged in as defaults on load; a registered entry of
# the same name wins, so `project_setup.py apex-core -d ...` can still override them.
BUILTIN = {
    "example": {
        "original": os.path.join(MODULE, "examples/demo/original"),
        "refactored": os.path.join(MODULE, "examples/demo/refactored"),
        "java": "8",
        "builtin": True,
    },
    "apex-core": {
        "original": os.path.join(MODULE, "examples/apex-core/original"),
        "refactored": os.path.join(MODULE, "examples/apex-core/refactored"),
        "java": "8",
        "builtin": True,
    },
}


def load():
    """Registered entries merged over the bundled ones."""
    out = {k: dict(v) for k, v in BUILTIN.items()}
    if os.path.isfile(REGISTRY):
        with open(REGISTRY) as f:
            try:
                saved = json.load(f)
            except json.JSONDecodeError as e:
                raise SystemExit(f"{REGISTRY} is not valid JSON: {e}")
        for name, entry in saved.items():
            out[name] = entry
    return out


def get(name):
    return load().get(name)


def put(name, **fields):
    """Add or update one entry, preserving fields the caller did not pass."""
    saved = {}
    if os.path.isfile(REGISTRY):
        with open(REGISTRY) as f:
            saved = json.load(f)
    entry = saved.get(name, {})
    entry.update({k: v for k, v in fields.items() if v is not None})
    entry["registered"] = datetime.now().isoformat(timespec="seconds")
    saved[name] = entry
    with open(REGISTRY, "w") as f:
        json.dump(saved, f, indent=2, sort_keys=True)
        f.write("\n")
    return entry


def drop(name):
    """Remove one registered entry. Returns whether it was there. Bundled entries cannot be
    dropped — they are defaults, not records."""
    if not os.path.isfile(REGISTRY):
        return False
    with open(REGISTRY) as f:
        saved = json.load(f)
    if name not in saved:
        return False
    del saved[name]
    with open(REGISTRY, "w") as f:
        json.dump(saved, f, indent=2, sort_keys=True)
        f.write("\n")
    return True


def resolve_trees(name, original=None, refactored=None):
    """(original, refactored) for a project: explicit arguments win, else the registry.

    Raises SystemExit with the exact command to fix it rather than letting a later step fail
    on a tree that was never there.
    """
    entry = get(name) or {}
    orig = original or entry.get("original")
    ref = refactored or entry.get("refactored")
    if not orig or not ref:
        known = ", ".join(sorted(load())) or "(none)"
        raise SystemExit(
            f"project '{name}' has no source trees.\n"
            f"  Pass them:     --original <origTree> --refactored <refTree>\n"
            f"  Or register:   python3 scripts/project_setup.py {name} -d <projectDir> \\\n"
            f"                     --original <origTree> --refactored <refTree>\n"
            f"  Registered:    {known}")
    for label, tree in (("--original", orig), ("--refactored", ref)):
        if not os.path.isdir(tree):
            raise SystemExit(f"{label} tree does not exist: {tree}")
    return os.path.abspath(orig), os.path.abspath(ref)


def java_release(name):
    """The JDK feature release this project's snapshots must be compiled with, or None."""
    return (get(name) or {}).get("java")


def _cli():
    """Shell-facing queries, so scripts/run_tmux.sh needs no JSON parser of its own.

        python3 scripts/projects.py                 # list every known project
        python3 scripts/projects.py <name> <field>  # one field, empty + exit 1 if unset
    """
    import sys
    if len(sys.argv) == 3:
        entry = get(sys.argv[1]) or {}
        value = entry.get(sys.argv[2], "")
        print(value)
        return 0 if value else 1
    reg = load()
    if not reg:
        print("no projects registered — see scripts/project_setup.py")
        return 0
    width = max(len(n) for n in reg)
    for n, e in sorted(reg.items()):
        tag = "  (bundled)" if e.get("builtin") else ""
        print(f"{n:<{width}}  java={e.get('java', '?'):<3} {e.get('original', '(no tree recorded)')}{tag}")
    return 0


if __name__ == "__main__":
    raise SystemExit(_cli())
