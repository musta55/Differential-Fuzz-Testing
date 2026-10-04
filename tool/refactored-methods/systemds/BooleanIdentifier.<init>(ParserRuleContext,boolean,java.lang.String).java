public BooleanIdentifier(ParserRuleContext ctx, boolean val, String filename) {
    this(val, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine(), ctx.getStop().getLine(), ctx.getStop().getCharPositionInLine(), filename);
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

