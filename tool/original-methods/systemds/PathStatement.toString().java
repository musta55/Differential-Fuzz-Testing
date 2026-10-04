@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(Statement.SETWD + "(");
    sb.append(_pathValue);
    sb.append(")");
    sb.append(";");
    return sb.toString();
}