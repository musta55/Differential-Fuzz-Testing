public CompressedSizeInfoColGroup getOrCreate(ColIndexes cI, ColIndexes c1, ColIndexes c2) {
    st2++;
    CompressedSizeInfoColGroup g = get(cI);
    if (g == null) {
        CompressedSizeInfoColGroup left = get(c1);
        CompressedSizeInfoColGroup right = get(c2);
        if (left != null && right != null) {
            st3++;
            g = _sEst.combine(cI._indexes, left, right);
            if (g != null && g.getNumVals() < 0) {
                throw new DMLCompressionException("Combination returned less distinct values on: \n" + left + "\nand\n" + right + "\nEq\n" + g);
            }
            synchronized (this) {
                if (get(cI) == null) {
                    // Double-check locking
                    put(cI, g);
                }
            }
        }
    }
    return g;
}