@Override
public final AOffset appendN(AOffsetsGroup[] g, int s) {
    for (AOffsetsGroup gs : g) {
        final AOffset a = gs.getOffsets();
        if (!(a instanceof OffsetByte))
            return super.appendN(g, s);
    }
    // calculate byte array size.
    int totalLength = g[0].getOffsets().getLength();
    for (int i = 1; i < g.length; i++) {
        totalLength += g[i].getOffsets().getLength() + 1;
        int remainder = s - g[i - 1].getOffsets().getOffsetToLast();
        totalLength += (remainder + g[i].getOffsets().getOffsetToFirst() - 1) / maxV;
    }
    final byte[] ret = new byte[totalLength];
    int p = 0;
    int remainderLast = 0;
    int size = 0;
    boolean first = true;
    for (AOffsetsGroup gs : g) {
        final OffsetByte b = (OffsetByte) gs.getOffsets();
        if (!first) {
            final int offFirst = remainderLast + b.offsetToFirst;
            final int div = offFirst / OffsetByte.maxV;
            final int mod = offFirst % OffsetByte.maxV;
            if (mod == 0) {
                // skip values
                p += div - 1;
                ret[p++] = (byte) OffsetByte.maxV;
            } else {
                // skip values
                p += div;
                ret[p++] = (byte) (mod);
            }
        }
        final byte[] bd = b.offsets;
        System.arraycopy(bd, 0, ret, p, bd.length);
        remainderLast = s - b.offsetToLast;
        size += b.size;
        p += bd.length;
        first = false;
    }
    final int offLast = s * (g.length - 1) + g[g.length - 1].getOffsets().getOffsetToLast();
    return new OffsetByte(ret, offsetToFirst, offLast, size);
}