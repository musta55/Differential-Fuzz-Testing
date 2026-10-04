@Override
public void read(DataInput in) throws IOException {
    int count = in.readInt();
    values = new Object[count];
    sizes = new long[count];
    totalSize = 0;
    for (int i = 0; i < count; i++) {
        sizes[i] = in.readLong();
        values[i] = SpillableObjectRegistry.read(in);
        totalSize += sizes[i];
    }
}