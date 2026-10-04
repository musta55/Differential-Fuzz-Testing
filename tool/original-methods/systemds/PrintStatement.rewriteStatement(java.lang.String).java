@Override
public Statement rewriteStatement(String prefix) {
    List<Expression> newExpressions = new ArrayList<>();
    for (Expression oldExpression : expressions) {
        Expression newExpression = oldExpression.rewriteExpression(prefix);
        newExpressions.add(newExpression);
    }
    PrintStatement retVal = new PrintStatement(_type, newExpressions);
    retVal.setParseInfo(this);
    return retVal;
}