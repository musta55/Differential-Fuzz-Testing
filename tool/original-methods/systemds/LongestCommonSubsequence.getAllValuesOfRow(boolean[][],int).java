private ArrayList<Integer> getAllValuesOfRow(boolean[][] lcsMatrix, int rowIndex) {
    ArrayList<Integer> result = new ArrayList<>();
    int index = 0;
    for (Boolean b : lcsMatrix[rowIndex]) {
        if (b)
            result.add(index);
        index++;
    }
    return result;
}