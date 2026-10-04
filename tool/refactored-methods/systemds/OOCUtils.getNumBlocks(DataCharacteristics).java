public static long getNumBlocks(DataCharacteristics dc) {
    if (dc == null || !dc.dimsKnown() || dc.getBlocksize() <= 0) {
        return -1;
    }
    return dc.getCols() == 0 || dc.getRows() == 0 ? 0 : dc.getNumBlocks();
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

