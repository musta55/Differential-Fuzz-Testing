private ArrayList<Integer> getAllValuesOfCol(boolean[][] lcsMatrix, int colIndex) {
    ArrayList<Integer> result = new ArrayList<>();
    int index = 0;
    for (boolean[] matrix : lcsMatrix) {
        if (matrix[colIndex])
            result.add(index);
        index++;
    }
    return result;
}