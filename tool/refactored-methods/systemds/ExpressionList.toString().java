@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(super.toString());
    sb.append("[");
    for (Expression e : value) {
        sb.append(e);
    }
    sb.append("]");
    return sb.toString();
}