public static DeltaDictionary read(DataInput in) throws IOException {
    int numCols = in.readInt();
    int numValues = in.readInt();
    double[] values = new double[numValues];
    for (int i = 0; i < numValues; i++) values[i] = in.readDouble();
    return new DeltaDictionary(values, numCols);
}