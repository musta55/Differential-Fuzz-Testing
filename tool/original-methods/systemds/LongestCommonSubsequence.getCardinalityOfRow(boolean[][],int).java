private int getCardinalityOfRow(boolean[][] lcsMatrix, int rowIndex) {
    int c = 0;
    for (Boolean b : lcsMatrix[rowIndex]) if (b)
        c++;
    return c;
}