/**
 * Sort (order) a compressed matrix in place of the {@code order} built-in, while keeping the result compressed.
 *
 * The compressed fast-path only supports the case the user can benefit from: a single column held in a single column
 * group, sorted ascending and returning the sorted values (not the index permutation). For everything else (multiple
 * columns, multiple column groups, descending order, index return, or a column-group encoding without a sort
 * implementation) this returns {@code null} so the caller can fall back to a decompressed reorg.
 *
 * @param mb the compressed matrix to sort
 * @param fn the sort specification carried by the reorg operator
 * @return the sorted compressed matrix, or {@code null} if the compressed fast-path does not apply
 */
public static MatrixBlock sort(CompressedMatrixBlock mb, SortIndex fn) {
    final boolean singleColumn = mb.getNumColumns() == 1 && mb.getColGroups().size() == 1;
    if (!singleColumn || fn.getDecreasing() || fn.getIndexReturn())
        return null;
    final AColGroup sorted = sortSingleColumn(mb);
    if (sorted == null)
        return null;
    final List<AColGroup> rg = new ArrayList<>(1);
    rg.add(sorted);
    return new CompressedMatrixBlock(mb.getNumRows(), mb.getNumColumns(), mb.getNonZeros(), false, rg);
}