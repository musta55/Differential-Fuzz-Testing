@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    appendClassName(sb);
    appendBuckets(sb);
    return sb.toString();
}
// ---- helper method(s) introduced by the refactoring ----
private void initializeKeysAndValues(int size) {
    keys = createKeys(size);
    values = createValues(size);
}

private void appendClassName(StringBuilder sb) {
    sb.append(this.getClass().getSimpleName());
    sb.append(" ");
}

private void appendBuckets(StringBuilder sb) {
    for (int i = 0; i < keys.length; i++) {
        if (keys[i] != null) {
            sb.append(String.format("\nB:%d: ", i));
            appendBucketContents(sb, i);
        }
    }
    removeTrailingComma(sb);
}

private void appendBucketContents(StringBuilder sb, int i) {
    for (int j = 0; j < keys[i].length; j++) {
        if (keys[i][j] != -1)
            sb.append(String.format("%d->%d, ", keys[i][j], values[i][j]));
    }
}

private void removeTrailingComma(StringBuilder sb) {
    if (sb.length() > 2) {
        sb.delete(sb.length() - 2, sb.length());
    }
}

