public IterablePredicate(ParserRuleContext ctx, DataIdentifier iterVar, Expression fromExpr, Expression toExpr, Expression incrementExpr, HashMap<String, String> parForParamValues, String filename) {
    _iterVar = iterVar;
    _fromExpr = fromExpr;
    _toExpr = toExpr;
    _incrementExpr = incrementExpr;
    _parforParams = parForParamValues;
    setCtxValuesAndFilename(ctx, filename);
}