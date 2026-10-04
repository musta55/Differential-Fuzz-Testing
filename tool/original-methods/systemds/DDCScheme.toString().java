@Override
public final String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.getClass().getSimpleName());
    sb.append("\nCols: ");
    sb.append(cols);
    sb.append("\nMap:  ");
    sb.append(getMap());
    return sb.toString();
}