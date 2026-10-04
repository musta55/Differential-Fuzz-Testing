@Override
public void read(DataInput in) throws IOException {
    int count = in.readInt();
    values = new Object[count];
    sizes = new long[count];
    totalSize = 0;
    for (int i = 0; i < count; i++) {
        readValue(in, i);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private boolean writeValue(DataOutput out, int index) throws IOException {
    out.writeLong(sizes[index]);
    Object value = values[index];
    if (!(value instanceof SpillableObject spillable)) {
        return false;
    }
    return SpillableObjectRegistry.tryWrite(out, spillable);
}

private void readValue(DataInput in, int index) throws IOException {
    sizes[index] = in.readLong();
    values[index] = SpillableObjectRegistry.read(in);
    totalSize += sizes[index];
}

