public static ParseInfo ctxAndFilenameToParseInfo(ParserRuleContext ctx, String fname) {
    ParseInfo pi = new ParseInfoImpl();
    setContextInfo(pi, ctx);
    pi.setFilename(fname);
    return pi;
}
// ---- helper method(s) introduced by the refactoring ----
private static void setContextInfo(ParseInfo pi, ParserRuleContext ctx) {
    pi.setBeginLine(ctx.start.getLine());
    pi.setBeginColumn(ctx.start.getCharPositionInLine());
    pi.setEndLine(ctx.stop.getLine());
    pi.setEndColumn(ctx.stop.getCharPositionInLine());
    // preserve whitespace if possible
    String text = extractText(ctx);
    if (text != null) {
        text = text.trim();
    }
    pi.setText(text);
}

private static String extractText(ParserRuleContext ctx) {
    if ((ctx.start != null) && (ctx.stop != null) && (ctx.start.getStartIndex() != -1) && (ctx.stop.getStopIndex() != -1) && (ctx.start.getStartIndex() <= ctx.stop.getStopIndex()) && (ctx.start.getInputStream() != null)) {
        return ctx.start.getInputStream().getText(Interval.of(ctx.start.getStartIndex(), ctx.stop.getStopIndex()));
    } else {
        return ctx.getText();
    }
}

