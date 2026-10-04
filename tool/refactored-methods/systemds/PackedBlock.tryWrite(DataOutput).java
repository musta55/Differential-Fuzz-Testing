@Override
public boolean tryWrite(DataOutput out) throws IOException {
    out.writeInt(values.length);
    for (int i = 0; i < values.length; i++) {
        if (!writeValue(out, i)) {
            return false;
        }
    }
    return true;
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

