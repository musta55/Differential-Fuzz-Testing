@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.getClass().getSimpleName());
    sb.append(" ");
    for (int i = 0; i < keys.length; i++) {
        if (keys[i] != null) {
            sb.append(String.format("\nB:%d: ", i));
            for (int j = 0; j < keys[i].length; j++) {
                if (keys[i][j] != -1)
                    sb.append(String.format("%d->%d, ", keys[i][j], values[i][j]));
            }
        }
    }
    return sb.delete(sb.length() - 2, sb.length()).toString();
}