@Override
public synchronized String toString() {
    StringBuilder sb = new StringBuilder();
    appendQueueHeader(sb);
    appendTasks(sb);
    return sb.toString();
}
// ---- helper method(s) introduced by the refactoring ----
private void appendQueueHeader(StringBuilder sb) {
    sb.append("TASK QUEUE (size=");
    sb.append(_data.size());
    sb.append(",close=");
    sb.append(_closedInput);
    sb.append(")\n");
}

private void appendTasks(StringBuilder sb) {
    int count = 1;
    for (T t : _data) {
        sb.append("  TASK #");
        sb.append(count);
        sb.append(": ");
        sb.append(t.toString());
        sb.append("\n");
        count++;
    }
}

