// rewrites statement to support function inlining (create deep copy)
@Override
public Statement rewriteStatement(String prefix) {
    OutputStatement newStatement = createDeepCopy(prefix);
    newStatement._id = (DataIdentifier) _id.rewriteExpression(prefix);
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
// ---- helper method(s) introduced by the refactoring ----
private OutputStatement createDeepCopy(String prefix) {
    return new OutputStatement(null, Expression.DataOp.WRITE, this);
}

