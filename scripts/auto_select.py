#!/usr/bin/env python3
"""
Source-text helpers shared by build_project.py.

Method discovery, the changed/unchanged decision and parameter classification all moved to
scripts/MethodExtractor.java, which parses a real AST with JavaParser. The heuristic parser that
used to live here (blank comments and literals, then brace-match with a signature regex) is gone:
it silently dropped every method preceded by an annotation with a string argument, because the
regex matched the annotation with a `params` group running past its own closing paren into the real
signature, and finditer does not revisit consumed text.

What remains are the two things that still work on raw text rather than on the tree: finding the
package, and locating top-level type spans so a secondary class can be lifted out of a snapshot
without reformatting the file the way printing an AST back out would.
"""
import re


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
