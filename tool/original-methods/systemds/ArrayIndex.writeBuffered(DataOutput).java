public void writeBuffered(DataOutput out) throws IOException {
    byte[] o = new byte[cols.length * 4 + 4 + 1];
    o[0] = (byte) ColIndexType.ARRAY.ordinal();
    IOUtilFunctions.intToBa(cols.length, o, 1);
    for (int i = 0; i < cols.length; i++) IOUtilFunctions.intToBa(cols[i], o, i * 4 + 5);
    out.write(o);
}