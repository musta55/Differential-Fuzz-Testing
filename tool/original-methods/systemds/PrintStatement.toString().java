@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(_type + "(");
    if ((_type == PRINTTYPE.PRINT) || (_type == PRINTTYPE.STOP) || (_type == PRINTTYPE.ASSERT)) {
        Expression expression = expressions.get(0);
        if (expression instanceof StringIdentifier) {
            sb.append("\"");
            sb.append(expression.toString());
            sb.append("\"");
        } else {
            sb.append(expression.toString());
        }
    } else if (_type == PRINTTYPE.PRINTF) {
        for (int i = 0; i < expressions.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            Expression expression = expressions.get(i);
            if (expression instanceof StringIdentifier) {
                sb.append("\"");
                sb.append(expression.toString());
                sb.append("\"");
            } else {
                sb.append(expression.toString());
            }
        }
    }
    sb.append(");");
    return sb.toString();
}