private static MatrixBlock rmEmptyRows(CompressedMatrixBlock in, MatrixBlock ret, boolean emptyReturn, MatrixBlock select) {
    if (select == null)
        return fallback(in, true, emptyReturn, select, ret);
    select = CompressedMatrixBlock.getUncompressed(select, "decompressing selection in rmempty");
    int rOut = (int) select.getNonZeros();
    if (rOut == -1)
        rOut = (int) select.recomputeNonZeros();
    if (rOut == 0) {
        ret.reset(!emptyReturn ? 0 : 1, in.getNumColumns());
        return ret;
    }
    // TODO: add optimization to avoid linear scan and make selectV indexes, if selection is small relative to number
    // of rows
    // TODO: add decompress to boolean vector.
    final boolean[] selectV = DataConverter.convertToBooleanVector(select);
    final List<AColGroup> inG = in.getColGroups();
    final List<AColGroup> retG = new ArrayList<>(inG.size());
    try {
        for (int i = 0; i < inG.size(); i++) {
            retG.add(inG.get(i).removeEmptyRows(selectV, rOut));
        }
    } catch (NotImplementedException e) {
        // Some column-group encodings (e.g. OLE/RLE) do not support index-only row removal;
        // decompress and remove on the uncompressed representation instead of failing.
        return fallback(in, true, emptyReturn, select, ret);
    }
    return new CompressedMatrixBlock(rOut, in.getNumColumns(), -1, in.isOverlapping(), retG);
}