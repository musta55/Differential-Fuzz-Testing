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
# Boxed wrappers the GenericDifferential engine can synthesize directly (buildOne handles
# Integer/Long/.../Boolean). The engine has always supported these at runtime; the manifest
# filter used to reject them, so signature-preserving methods taking a boxed arg were dropped.
BOXED = {"Integer", "Long", "Short", "Byte", "Character", "Boolean", "Float", "Double"}
SCALAR = PRIMS | {"String"} | {p + "[]" for p in PRIMS} | BOXED

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


_TYPE_KW = re.compile(r"\b(?:class|interface|enum|record)\b\s+([A-Za-z_$][\w$]*)")
_MODIFIERS = {"public", "protected", "private", "abstract", "final", "static",
              "sealed", "non-sealed", "strictfp"}


def _extend_decl_start(src, kw_idx):
    """Walk left from a type keyword over its modifiers/annotations so the returned index is
    the true start of the declaration (else strip_spans would orphan a leading `final`/`@Ann`)."""
    start = kw_idx
    while start > 0:
        j = start
        while j > 0 and src[j - 1] in " \t\r\n":
            j -= 1
        if j == 0:
            break
        if src[j - 1] == ")":  # annotation with args: @Ann(...)
            depth, p = 0, j - 1
            while p >= 0:
                if src[p] == ")":
                    depth += 1
                elif src[p] == "(":
                    depth -= 1
                    if depth == 0:
                        break
                p -= 1
            q = p
            while q > 0 and (src[q - 1].isalnum() or src[q - 1] in "_$."):
                q -= 1
            if q > 0 and src[q - 1] == "@":
                start = q - 1; continue
            break
        k = j
        while k > 0 and (src[k - 1].isalnum() or src[k - 1] in "_$-"):
            k -= 1
        word = src[k:j]
        if word in _MODIFIERS:
            start = k; continue
        if word and k > 0 and src[k - 1] == "@":  # marker annotation: @Ann
            start = k - 1; continue
        break
    return start


def _find_body_brace(src, start):
    """Index of the first '{' at/after start, skipping comments (type headers hold no strings)."""
    n, j = len(src), start
    while j < n:
        two, c = src[j:j + 2], src[j]
        if two == "//":
            k = src.find("\n", j); j = n if k < 0 else k; continue
        if two == "/*":
            k = src.find("*/", j + 2); j = n if k < 0 else k + 2; continue
        if c == "{":
            return j
        j += 1
    return -1


def _match_close_brace(src, open_idx):
    """Index just AFTER the '}' matching the '{' at open_idx (comment/string aware)."""
    n, j, depth = len(src), open_idx, 0
    while j < n:
        two, c = src[j:j + 2], src[j]
        if two == "//":
            k = src.find("\n", j); j = n if k < 0 else k; continue
        if two == "/*":
            k = src.find("*/", j + 2); j = n if k < 0 else k + 2; continue
        if c == '"':
            k = j + 1
            while k < n and src[k] != '"':
                k += 2 if src[k] == "\\" else 1
            j = k + 1; continue
        if c == "'":
            k = j + 1
            while k < n and src[k] != "'":
                k += 2 if src[k] == "\\" else 1
            j = k + 1; continue
        if c == "{":
            depth += 1
        elif c == "}":
            depth -= 1
            if depth == 0:
                return j + 1
        j += 1
    return n


def top_level_type_spans(src):
    """[(typeName, startIdx, endIdx)] for each TOP-LEVEL class/interface/enum/record in src.

    endIdx is just past the type's closing brace. Comment/string aware; nested types are
    skipped because we jump past each top-level body. Used to detect and relocate secondary
    top-level classes so <Class>{Original,Refactored} snapshots don't collide on them.
    """
    spans, n, i = [], len(src), 0
    while i < n:
        two, c = src[i:i + 2], src[i]
        if two == "//":
            k = src.find("\n", i); i = n if k < 0 else k; continue
        if two == "/*":
            k = src.find("*/", i + 2); i = n if k < 0 else k + 2; continue
        if c == '"':
            k = i + 1
            while k < n and src[k] != '"':
                k += 2 if src[k] == "\\" else 1
            i = k + 1; continue
        if c == "'":
            k = i + 1
            while k < n and src[k] != "'":
                k += 2 if src[k] == "\\" else 1
            i = k + 1; continue
        m = _TYPE_KW.match(src, i)
        if m:
            brace = _find_body_brace(src, m.end())
            if brace < 0:
                break
            end = _match_close_brace(src, brace)
            spans.append((m.group(1), _extend_decl_start(src, m.start()), end))
            i = end; continue
        i += 1
    return spans


def strip_spans(text, spans):
    """Remove the given (name, start, end) spans from text (highest offset first)."""
    for _name, start, end in sorted(spans, key=lambda s: s[1], reverse=True):
        text = text[:start] + text[end:]
    return text
