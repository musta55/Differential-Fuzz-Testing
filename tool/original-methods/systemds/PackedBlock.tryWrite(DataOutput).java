@Override
public boolean tryWrite(DataOutput out) throws IOException {
    out.writeInt(values.length);
    for (int i = 0; i < values.length; i++) {
        out.writeLong(sizes[i]);
        Object value = values[i];
        if (!(value instanceof SpillableObject spillable))
            return false;
        if (!SpillableObjectRegistry.tryWrite(out, spillable))
            return false;
    }
    return true;
}