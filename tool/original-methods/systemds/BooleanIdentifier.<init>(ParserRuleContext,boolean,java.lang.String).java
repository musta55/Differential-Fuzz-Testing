public BooleanIdentifier(ParserRuleContext ctx, boolean val, String filename) {
    this(val);
    setCtxValuesAndFilename(ctx, filename);
}