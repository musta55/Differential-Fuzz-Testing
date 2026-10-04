@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.getClass().getSimpleName());
    sb.append("[");
    sb.append(idx);
    sb.append("]");
    return sb.toString();
}