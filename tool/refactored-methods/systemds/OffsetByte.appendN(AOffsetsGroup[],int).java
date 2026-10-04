@Override
public final AOffset appendN(AOffsetsGroup[] g, int s) {
    if (!allGroupsAreOffsetByte(g)) {
        return super.appendN(g, s);
    }
    int totalLength = calculateTotalLength(g, s);
    byte[] ret = new byte[totalLength];
    int p = 0;
    int remainderLast = 0;
    int newSize = 0;
    boolean first = true;
    for (AOffsetsGroup gs : g) {
        OffsetByte b = (OffsetByte) gs.getOffsets();
        if (!first) {
            appendOffset(ret, p, remainderLast, b.offsetToFirst);
            p += getBytesToAdd(remainderLast, b.offsetToFirst);
        }
        System.arraycopy(b.offsets, 0, ret, p, b.offsets.length);
        remainderLast = s - b.offsetToLast;
        newSize += b.size;
        p += b.offsets.length;
        first = false;
    }
    int offLast = s * (g.length - 1) + g[g.length - 1].getOffsets().getOffsetToLast();
    return new OffsetByte(ret, offsetToFirst, offLast, newSize);
}
// ---- helper method(s) introduced by the refactoring ----
private boolean allGroupsAreOffsetByte(AOffsetsGroup[] g) {
    for (AOffsetsGroup gs : g) {
        if (!(gs.getOffsets() instanceof OffsetByte)) {
            return false;
        }
    }
    return true;
}

private int calculateTotalLength(AOffsetsGroup[] g, int s) {
    int totalLength = g[0].getOffsets().getLength();
    for (int i = 1; i < g.length; i++) {
        totalLength += g[i].getOffsets().getLength() + 1;
        int remainder = s - g[i - 1].getOffsets().getOffsetToLast();
        totalLength += (remainder + g[i].getOffsets().getOffsetToFirst() - 1) / maxV;
    }
    return totalLength;
}

private void appendOffset(byte[] ret, int p, int remainderLast, int offsetToFirst) {
    int offFirst = remainderLast + offsetToFirst;
    int div = offFirst / OffsetByte.maxV;
    int mod = offFirst % OffsetByte.maxV;
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

private int getBytesToAdd(int remainderLast, int offsetToFirst) {
    int offFirst = remainderLast + offsetToFirst;
    int div = offFirst / OffsetByte.maxV;
    int mod = offFirst % OffsetByte.maxV;
    return mod == 0 ? div : div + 1;
}

