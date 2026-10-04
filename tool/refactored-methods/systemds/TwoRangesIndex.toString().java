@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.getClass().getSimpleName());
    sb.append("[");
    sb.append(idx1.get(0));
    sb.append(" -> ");
    sb.append(idx1.get(idx1.size() - 1));
    sb.append(" And ");
    sb.append(idx2.get(0));
    sb.append(" -> ");
    sb.append(idx2.get(idx2.size() - 1));
    sb.append("]");
    return sb.toString();
}