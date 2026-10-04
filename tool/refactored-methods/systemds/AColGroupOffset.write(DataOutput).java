@Override
public void write(DataOutput out) throws IOException {
    writeHeader(out);
    writeBitmaps(out);
}
// ---- helper method(s) introduced by the refactoring ----
private static void compactBitmaps(int[] bitmap, char[] data, char[][] lbitmaps) {
    // compact bitmaps to linearized representation
    for (int i = 0, off = 0; i < bitmap.length - 1; i++) {
        int len = lbitmaps[i].length;
        bitmap[i] = off;
        System.arraycopy(lbitmaps[i], 0, data, off, len);
        off += len;
    }
    bitmap[bitmap.length - 1] = data.length;
}

private int countOffsets(boolean[] ind) {
    // determine number of offsets
    int numOffsets = 0;
    for (int i = 0; i < ind.length; i++) numOffsets += ind[i] ? 1 : 0;
    return numOffsets;
}

private int[] createOffsetList(boolean[] ind, int numOffsets) {
    // create offset lists
    int[] ret = new int[numOffsets];
    for (int i = 0, pos = 0; i < ind.length; i++) if (ind[i])
        ret[pos++] = i;
    return ret;
}

private void writeHeader(DataOutput out) throws IOException {
    super.write(out);
}

private void writeBitmaps(DataOutput out) throws IOException {
    // write bitmaps (lens and data, offset later recreated)
    out.writeInt(_ptr.length);
    for (int i = 0; i < _ptr.length; i++) out.writeInt(_ptr[i]);
    out.writeInt(_data.length);
    for (int i = 0; i < _data.length; i++) out.writeChar(_data[i]);
    out.writeBoolean(_zeros);
}

