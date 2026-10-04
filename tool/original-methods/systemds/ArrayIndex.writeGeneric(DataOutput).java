public void writeGeneric(DataOutput out) throws IOException {
    byte[] o = new byte[512];
    o[0] = (byte) ColIndexType.ARRAY.ordinal();
    IOUtilFunctions.intToBa(cols.length, o, 1);
    out.write(o, 0, 5);
    int i = 0;
    while (i + 512 / 4 < cols.length) {
        for (int of = 0; of < o.length; of += 4, i++) IOUtilFunctions.intToBa(cols[i], o, of);
        out.write(o);
    }
    int of = 0;
    for (; i < cols.length; of += 4, i++) IOUtilFunctions.intToBa(cols[i], o, of);
    out.write(o, 0, of);
}