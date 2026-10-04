@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(Statement.SOURCE).append("(");
    sb.append(_filePath).append(")");
    if (_namespace != null) {
        sb.append(" AS ").append(_namespace);
    }
    sb.append(";");
    return sb.toString();
}