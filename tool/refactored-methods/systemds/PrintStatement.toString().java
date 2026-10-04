@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(_type + "(");
    appendExpressions(sb);
    sb.append(");");
    return sb.toString();
}
// ---- helper method(s) introduced by the refactoring ----
private List<Expression> rewriteExpressions(List<Expression> expressions, String prefix) {
    List<Expression> newExpressions = new ArrayList<>();
    for (Expression oldExpression : expressions) {
        newExpressions.add(oldExpression.rewriteExpression(prefix));
    }
    return newExpressions;
}

private void appendExpressions(StringBuilder sb) {
    if (_type == PRINTTYPE.PRINT || _type == PRINTTYPE.STOP || _type == PRINTTYPE.ASSERT) {
        appendExpression(sb, expressions.get(0));
    } else if (_type == PRINTTYPE.PRINTF) {
        for (int i = 0; i < expressions.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            appendExpression(sb, expressions.get(i));
        }
    }
}

private void appendExpression(StringBuilder sb, Expression expression) {
    if (expression instanceof StringIdentifier) {
        sb.append("\"").append(expression.toString()).append("\"");
    } else {
        sb.append(expression.toString());
    }
}

