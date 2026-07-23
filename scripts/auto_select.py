#!/usr/bin/env python3
"""
Pure Java-parsing helpers shared by build_project.py.

A heuristic parser (strip comments + literals, then brace-match) that extracts each
top-level method's normalized body + signature, and classifies whether every parameter
type is a scalar the GenericDifferential engine can synthesize from fuzz bytes
(primitives/boxes, String, primitive arrays). No project layout is assumed here — the
functions take Java source text and paths only.
"""
import re

KEYWORDS = {"if", "for", "while", "switch", "catch", "synchronized", "return",
            "new", "else", "do", "try", "finally", "case"}

PRIMS = {"int", "long", "short", "byte", "char", "boolean", "float", "double"}
SCALAR = PRIMS | {"String"} | {p + "[]" for p in PRIMS}

SIG_RE = re.compile(
    r'(?P<sig>(?P<pre>(?:[A-Za-z_$][\w$<>\[\],.\s?&]*?\s+)?)'
    r'(?P<name>[A-Za-z_$][\w$]*)\s*'
    r'\((?P<params>[^;{}]*)\)\s*'
    r'(?:throws\s[\w$.,\s]+?)?)\s*\{',
)


def strip_noise(src):
    out, i, n = [], 0, len(src)
    while i < n:
        c, two = src[i], src[i:i + 2]
        if two == "//":
            j = src.find("\n", i); i = n if j < 0 else j; continue
        if two == "/*":
            j = src.find("*/", i + 2); i = n if j < 0 else j + 2; continue
        if c == '"':
            j = i + 1
            while j < n and src[j] != '"':
                j += 2 if src[j] == "\\" else 1
            out.append('""'); i = j + 1; continue
        if c == "'":
            j = i + 1
            while j < n and src[j] != "'":
                j += 2 if src[j] == "\\" else 1
            out.append("' '"); i = j + 1; continue
        out.append(c); i += 1
    return "".join(out)


def methods(src):
    """name/paramCount -> (normalized_body, pre_modifiers, params_str). Top-level methods only."""
    clean = strip_noise(src)
    out = {}
    for m in SIG_RE.finditer(clean):
        name = m.group("name")
        if name in KEYWORDS:
            continue
        brace = clean.find("{", m.start())
        # Only top-level methods (primary class body == brace depth 1) can be addressed as
        # <PrimaryClass>.method; methods in a nested class/enum are at depth >= 2 -> skip.
        if clean.count("{", 0, brace) - clean.count("}", 0, brace) != 1:
            continue
        depth, j, n = 0, brace, len(clean)
        while j < n:
            if clean[j] == "{": depth += 1
            elif clean[j] == "}":
                depth -= 1
                if depth == 0: break
            j += 1
        params = m.group("params").strip()
        pcount = 0 if not params else len(split_params(params))
        key = f"{name}/{pcount}"
        base, k = key, 0
        while key in out:
            k += 1; key = f"{base}#{k}"
        out[key] = (re.sub(r"\s+", " ", clean[brace:j + 1]).strip(),
                    m.group("pre").strip(), params)
    return out


def split_params(params):
    parts, depth, cur = [], 0, ""
    for ch in params:
        if ch == "<": depth += 1
        elif ch == ">": depth -= 1
        if ch == "," and depth == 0:
            parts.append(cur); cur = ""
        else:
            cur += ch
    if cur.strip():
        parts.append(cur)
    return [p.strip() for p in parts if p.strip()]


def param_type(p):
    """Extract the (type) token of a single 'Type name' parameter, or None if not scalar."""
    if "<" in p or "@" in p or "..." in p:
        return None
    toks = [t for t in p.replace("\t", " ").split() if t != "final"]
    if len(toks) < 2:
        return None
    return toks[-2]  # token before the parameter name


def classify(pre, params):
    """Return (auto_fuzzable, is_static, [typeTokens]) for a method signature."""
    is_static = bool(re.search(r"\bstatic\b", pre))
    if not params:
        return True, is_static, []
    types = []
    for p in split_params(params):
        t = param_type(p)
        if t is None or t not in SCALAR:
            return False, is_static, []
        types.append(t)
    return True, is_static, types


def package(path):
    for line in open(path, encoding="utf-8", errors="replace"):
        m = re.match(r"\s*package\s+([\w.]+)\s*;", line)
        if m:
            return m.group(1)
    return None
