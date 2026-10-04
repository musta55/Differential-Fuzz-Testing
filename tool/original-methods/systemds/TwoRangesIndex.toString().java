@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.getClass().getSimpleName());
    sb.append("[");
    sb.append(idx1.get(0));
    sb.append(" -> ");
    sb.append(idx1.get(idx1.size()));
    sb.append(" And ");
    sb.append(idx2.get(0));
    sb.append(" -> ");
    sb.append(idx2.get(idx2.size()));
    sb.append("]");
    return sb.toString();
}