public BooleanIdentifier(boolean val, ParseInfo parseInfo) {
    this(val, -1, -1, -1, -1, null);
    setParseInfo(parseInfo);
}
// ---- helper method(s) introduced by the refactoring ----
private BooleanIdentifier(boolean val, int beginLine, int beginColumn, int endLine, int endColumn, String text) {
    super();
    setInfo(val);
    setBeginLine(beginLine);
    setBeginColumn(beginColumn);
    setEndLine(endLine);
    setEndColumn(endColumn);
    setText(text);
}

