@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");
    sb.append(_iterVar.getName());
    sb.append(" in seq(");
    sb.append(_fromExpr.toString());
    sb.append(",");
    sb.append(_toExpr.toString());
    if (_incrementExpr != null) {
        sb.append(",");
        sb.append(_incrementExpr.toString());
    }
    sb.append(")");
    if (_parforParams != null && _parforParams.size() > 0) {
        for (String key : _parforParams.keySet()) {
            sb.append(",");
            sb.append(key);
            sb.append("=");
            sb.append(_parforParams.get(key));
        }
    }
    sb.append(")");
    return sb.toString();
}