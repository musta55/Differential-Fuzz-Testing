@Override
public void writeSparseRows(int rlen, SparseBlock rows) throws IOException {
    int lrlen = Math.min(rows.numRows(), rlen);
    for (int i = 0; i < lrlen; i++) {
        if (!rows.isEmpty(i)) {
            int apos = rows.pos(i);
            int alen = rows.size(i);
            int[] aix = rows.indexes(i);
            double[] avals = rows.values(i);
            writeInt(alen);
            for (int j = apos; j < apos + alen; j++) {
                writeInt(aix[j]);
                writeLong(Double.doubleToRawLongBits(avals[j]));
            }
        } else {
            writeInt(0);
        }
    }
    for (int i = lrlen; i < rlen; i++) writeInt(0);
}