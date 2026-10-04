@Override
public synchronized String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("TASK QUEUE (size=");
    sb.append(_data.size());
    sb.append(",close=");
    sb.append(_closedInput);
    sb.append(")\n");
    int count = 1;
    for (T t : _data) {
        sb.append("  TASK #");
        sb.append(count);
        sb.append(": ");
        sb.append(t.toString());
        sb.append("\n");
        count++;
    }
    return sb.toString();
}