protected static void createCompressedBitmaps(int[] bitmap, char[] data, char[][] lbitmaps) {
    // compact bitmaps to linearized representation
    for (int i = 0, off = 0; i < bitmap.length - 1; i++) {
        int len = lbitmaps[i].length;
        bitmap[i] = off;
        System.arraycopy(lbitmaps[i], 0, data, off, len);
        off += len;
    }
    bitmap[bitmap.length - 1] = data.length;
}