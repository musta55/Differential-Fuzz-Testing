private int getNextSetRow(boolean[][] lcsMatrix, int rowIndex, int colIndex) {
    int result = -1;
    for (int i = rowIndex; i < lcsMatrix.length && result == -1; i++) result = lcsMatrix[i][colIndex] ? i : -1;
    return result;
}