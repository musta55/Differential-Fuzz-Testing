private int getNextSetCol(boolean[][] lcsMatrix, int rowIndex, int colIndex) {
    int result = -1;
    for (int i = colIndex; i < lcsMatrix[0].length && result == -1; i++) result = lcsMatrix[rowIndex][i] ? i : -1;
    return result;
}