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


def strip_noise(src, keep_literals=False):
    """Blank out comments, and (unless keep_literals) the CONTENTS of string/char literals.

    LENGTH-PRESERVING: every masked character becomes a space and newlines survive, so an index
    into the result is the same index in `src`. That is what lets methods() parse structure from
    the literal-blanked text while taking the body it compares from the literal-preserving one.

    Two maskings are needed because they answer different questions. Structure must not see a `{`
    or `;` inside a string literal, or brace-matching walks off the end of the method. Comparison
    must not see comments, or rewording a javadoc marks every method in the file as changed. But
    comparison MUST see literal contents: blanking them made `return "foo"` and `return ""`
    normalize identically, so a method whose only change was its returned string was reported as
    unchanged and never fuzzed at all (github issue #3 — the tool printed "0 changed methods").
    """
    out = list(src)
    i, n = 0, len(src)
    while i < n:
        c, two = src[i], src[i:i + 2]
        if two == "//":
            j = src.find("\n", i)
            j = n if j < 0 else j
            for k in range(i, j):
                out[k] = " "
            i = j
            continue
        if two == "/*":
            j = src.find("*/", i + 2)
            j = n if j < 0 else j + 2
            for k in range(i, j):
                if out[k] != "\n":
                    out[k] = " "
            i = j
            continue
        if c == '"' or c == "'":
            j = i + 1
            while j < n and src[j] != c:
                j += 2 if src[j] == "\\" else 1
            if not keep_literals:
                for k in range(i + 1, min(j, n)):
                    if out[k] != "\n":
                        out[k] = " "
            i = j + 1
            continue
        i += 1
    return "".join(out)


def methods(src):
    """name/paramCount -> (normalized_body, pre_modifiers, params_str). Top-level methods only."""
    # `clean` drives every structural decision (signature regex, brace depth, brace matching);
    # `literal` is the same text with string/char contents intact and is what the recorded body is
    # sliced from, so a literal-only change still counts as a change. Both are length-preserving,
    # so an offset found in one indexes the other.
    clean = strip_noise(src)
    literal = strip_noise(src, keep_literals=True)
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
        # `... field = new Foo() { ... }` sits at brace depth 1 like a real method and matches
        # SIG_RE with pre=="new". It is an anonymous-class instantiation, not a method — the
        # resulting entry would only fail with NoSuchMethodException at fuzz time.
        if re.search(r"\bnew\s*$", m.group("pre")):
            continue
        # An annotation with arguments on its own line looks exactly like a signature once
        # strip_noise() has blanked the literal: `@SuppressWarnings("")` matched as the method
        # `SuppressWarnings` taking one parameter `""`. Seen for real in apex-core's
        # RecoverableRpcProxy, where it produced a bogus manifest entry.
        if m.start("name") > 0 and clean[m.start("name") - 1] == "@":
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
        out[key] = (re.sub(r"\s+", " ", literal[brace:j + 1]).strip(),
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
    """Source-level type of one `Type name` parameter, or None if unparseable.

    Annotations and `final` are stripped; generics are kept as written (the engine erases them).
    A varargs `T...` becomes `T[]`, which is what the compiled signature actually is.
    """
    p = re.sub(r"@[A-Za-z_$][\w$.]*(\s*\([^)]*\))?", " ", p)  # drop annotations
    p = p.replace("\t", " ").strip()
    varargs = "..." in p
    p = p.replace("...", " ")
    toks = [t for t in p.split() if t != "final"]
    if len(toks) < 2:
        return None
    t = " ".join(toks[:-1]).strip()  # everything before the parameter name
    return (t + "[]") if varargs else t


def classify(pre, params):
    """Return (testable, is_static, [sourceTypeNames]) for a method signature.

    Every parameter is now reported rather than filtered: the engine builds objects via Jazzer's
    autofuzz, so a `Configuration` or `Sink<Object>` argument is no longer a reason to drop the
    method. Half of apex-core's changed methods used to be discarded here and never tested at all.

    The returned names are the SOURCE spellings and are advisory only — the engine resolves the
    method by name and arity against the compiled class and reads the real parameter types by
    reflection, because resolving a simple name like `Configuration` to an FQN from source would
    mean reimplementing Java's import rules.

    `testable` is False only when the signature could not be parsed at all.
    """
    is_static = bool(re.search(r"\bstatic\b", pre))
    if not params:
        return True, is_static, []
    types = []
    for p in split_params(params):
        t = param_type(p)
        if t is None:
            return False, is_static, []
        types.append(t)
    return True, is_static, types


def all_scalar(types):
    """True when every parameter is a scalar the engine can build without autofuzz.

    Retained so the manifest can label a method `scalar` vs `object`, which is what lets the
    report show how much of the run depends on autofuzz.
    """
    return all(t in SCALAR for t in types)


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
