public static Collection<MatrixIndexes> getTilesOfRange(IndexRange range, long blen) {
    long rs = (range.rowStart - 1) / blen + 1;
    long re = (range.rowEnd - 1) / blen + 1;
    long cs = (range.colStart - 1) / blen + 1;
    long ce = (range.colEnd - 1) / blen + 1;
    if (rs == re) {
        return cs == ce ? Collections.singleton(new MatrixIndexes(rs, cs)) : getSingleRowTiles(rs, cs, ce);
    }
    return getAllTiles(rs, re, cs, ce);
}
// ---- helper method(s) introduced by the refactoring ----
private static Collection<MatrixIndexes> getSingleRowTiles(long row, long startCol, long endCol) {
    List<MatrixIndexes> list = new ArrayList<>((int) (endCol - startCol + 1));
    for (long col = startCol; col <= endCol; col++) {
        list.add(new MatrixIndexes(row, col));
    }
    return list;
}

private static Collection<MatrixIndexes> getAllTiles(long startRow, long endRow, long startCol, long endCol) {
    List<MatrixIndexes> list = new ArrayList<>((int) ((endRow - startRow + 1) * (endCol - startCol + 1)));
    for (long row = startRow; row <= endRow; row++) {
        for (long col = startCol; col <= endCol; col++) {
            list.add(new MatrixIndexes(row, col));
        }
    }
    return list;
}

