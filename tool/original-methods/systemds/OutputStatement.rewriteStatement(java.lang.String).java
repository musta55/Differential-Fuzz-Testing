// rewrites statement to support function inlining (create deep copy)
@Override
public Statement rewriteStatement(String prefix) {
    OutputStatement newStatement = new OutputStatement(null, Expression.DataOp.WRITE, this);
    // rewrite outputStatement variable name (creates deep copy)
    newStatement._id = (DataIdentifier) this._id.rewriteExpression(prefix);
    // rewrite parameter expressions (creates deep copy)
    DataOp op = _paramsExpr.getOpCode();
    HashMap<String, Expression> newExprParams = new HashMap<>();
    for (String key : _paramsExpr.getVarParams().keySet()) {
        Expression newExpr = _paramsExpr.getVarParam(key).rewriteExpression(prefix);
        newExprParams.put(key, newExpr);
    }
    DataExpression newParamerizedExpr = new DataExpression(op, newExprParams, this);
    newStatement.setExprParams(newParamerizedExpr);
    return newStatement;
}