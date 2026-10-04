@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.getClass().getSimpleName());
    sb.append("[");
    sb.append(l);
    sb.append(", ");
    sb.append(r);
    sb.append("]");
    return sb.toString();
}