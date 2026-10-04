private int getCardinalityOfCol(boolean[][] lcsMatrix, int colIndex) {
    int c = 0;
    for (boolean[] matrix : lcsMatrix) if (matrix[colIndex])
        c++;
    return c;
}