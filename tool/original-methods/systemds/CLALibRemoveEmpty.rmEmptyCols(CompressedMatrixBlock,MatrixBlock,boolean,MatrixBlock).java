private static MatrixBlock rmEmptyCols(CompressedMatrixBlock in, MatrixBlock ret, boolean emptyReturn, MatrixBlock select) {
    if (select == null)
        return fallback(in, false, emptyReturn, select, ret);
    int cOut = (int) select.getNonZeros();
    if (cOut == -1)
        cOut = (int) select.recomputeNonZeros();
    if (cOut == 0) {
        ret.reset(in.getNumRows(), !emptyReturn ? 0 : 1);
        return ret;
    }
    final boolean[] selectV = DataConverter.convertToBooleanVector(CompressedMatrixBlock.getUncompressed(select, "decompressing selection in rmempty"));
    final List<AColGroup> inG = in.getColGroups();
    final List<AColGroup> retG = new ArrayList<>(inG.size());
    try {
        for (int i = 0; i < inG.size(); i++) {
            AColGroup tmp = inG.get(i).removeEmptyCols(selectV);
            if (tmp != null)
                retG.add(tmp);
        }
    } catch (NotImplementedException e) {
        // Some column-group encodings (e.g. OLE/RLE) do not support index-only column removal;
        // decompress and remove on the uncompressed representation instead of failing.
        return fallback(in, false, emptyReturn, select, ret);
    }
    return new CompressedMatrixBlock(in.getNumRows(), cOut, -1, in.isOverlapping(), retG);
}