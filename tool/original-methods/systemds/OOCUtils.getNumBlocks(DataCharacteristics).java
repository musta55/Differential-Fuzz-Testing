public static long getNumBlocks(DataCharacteristics dc) {
    if (dc != null && dc.dimsKnown() && dc.getBlocksize() > 0) {
        if (dc.getCols() == 0 || dc.getRows() == 0)
            return 0;
        return dc.getNumBlocks();
    }
    return -1;
}