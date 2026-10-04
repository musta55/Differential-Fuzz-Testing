public CompressedSizeInfoColGroup getOrCreate(ColIndexes cI, ColIndexes c1, ColIndexes c2) {
    CompressedSizeInfoColGroup g = mem.get(cI);
    st2++;
    if (g == null) {
        final CompressedSizeInfoColGroup left = mem.get(c1);
        final CompressedSizeInfoColGroup right = mem.get(c2);
        if (left != null && right != null) {
            st3++;
            g = _sEst.combine(cI._indexes, left, right);
            if (g != null) {
                if (g.getNumVals() < 0)
                    throw new DMLCompressionException("Combination returned less distinct values on: \n" + left + "\nand\n" + right + "\nEq\n" + g);
            }
            synchronized (this) {
                mem.put(cI, g);
            }
        }
    }
    return g;
}