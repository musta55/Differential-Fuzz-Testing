public ArrayList<String> getLCS(String str1, String str2) {
    int m = str1.length();
    int n = str2.length();
    boolean[][] lcsMatrix = initializeMatrix(m, n);
    ArrayList<String> allLCS = new ArrayList<>();
    populateMatrix(lcsMatrix, str1, str2);
    findLCSInLayout(allLCS, lcsMatrix, str1, str2, true);
    findLCSInLayout(allLCS, lcsMatrix, str2, str1, false);
    return allLCS;
}
// ---- helper method(s) introduced by the refactoring ----
private boolean[][] initializeMatrix(int m, int n) {
    boolean[][] lcsMatrix = new boolean[m][n];
    for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) lcsMatrix[i][j] = false;
    return lcsMatrix;
}

private void populateMatrix(boolean[][] lcsMatrix, String str1, String str2) {
    int m = str1.length();
    int n = str2.length();
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            if (isDelimiter(str1.charAt(i)) || isDelimiter(str2.charAt(j))) {
                continue;
            }
            if (str1.charAt(i) == str2.charAt(j)) {
                lcsMatrix[i][j] = true;
            }
        }
    }
}

private void findLCSInLayout(ArrayList<String> allLCS, boolean[][] lcsMatrix, String str1, String str2, boolean isRowMajor) {
    int primaryLength = isRowMajor ? str1.length() : str2.length();
    int secondaryLength = isRowMajor ? str2.length() : str1.length();
    for (int i = 0; i < primaryLength; i++) {
        if (isDelimiter(isRowMajor ? str1.charAt(i) : str2.charAt(i)) || getCardinality(isRowMajor ? lcsMatrix[i] : getColumn(lcsMatrix, i), isRowMajor) == 0)
            continue;
        for (Integer j : getAllValues(isRowMajor ? lcsMatrix[i] : getColumn(lcsMatrix, i), isRowMajor)) {
            int li = i - 1;
            int lj = j - 1;
            if (li > 0 && lj > 0 && lcsMatrix[li][lj])
                continue;
            StringBuilder sb = new StringBuilder();
            sb.append(isRowMajor ? str1.charAt(i) : str2.charAt(i));
            int l = j + 1;
            for (int k = i + 1; k < primaryLength && l < secondaryLength; k++) {
                int ul = getNextSet(lcsMatrix, k, l, isRowMajor);
                if (getCardinality(isRowMajor ? lcsMatrix[k] : getColumn(lcsMatrix, k), isRowMajor) == 0 || ul == -1) {
                    if (!sb.toString().endsWith(Lop.OPERAND_DELIMITOR))
                        sb.append(Lop.OPERAND_DELIMITOR);
                    continue;
                }
                int lul = ul - 1;
                int lk = k - 1;
                if (ul - l > 0 || (lul > 0 && lk > 0 && !lcsMatrix[lk][lul])) {
                    if (!sb.toString().endsWith(Lop.OPERAND_DELIMITOR))
                        sb.append(Lop.OPERAND_DELIMITOR);
                }
                sb.append(isRowMajor ? str2.charAt(ul) : str1.charAt(ul));
                l = ul + 1;
            }
            if (sb.length() > 0 && !sb.toString().equals(Lop.OPERAND_DELIMITOR)) {
                allLCS.add(sb.toString());
            }
        }
    }
}

private boolean isDelimiter(char c) {
    return (c + "").equals(Lop.OPERAND_DELIMITOR);
}

private int getCardinality(boolean[] array, boolean isRowMajor) {
    int c = 0;
    for (boolean b : array) if (b)
        c++;
    return c;
}

private ArrayList<Integer> getAllValues(boolean[] array, boolean isRowMajor) {
    ArrayList<Integer> result = new ArrayList<>();
    for (int i = 0; i < array.length; i++) {
        if (array[i])
            result.add(i);
    }
    return result;
}

private int getNextSet(boolean[][] lcsMatrix, int index1, int index2, boolean isRowMajor) {
    int result = -1;
    int length = isRowMajor ? lcsMatrix[0].length : lcsMatrix.length;
    for (int i = index2; i < length && result == -1; i++) result = isRowMajor ? (lcsMatrix[index1][i] ? i : -1) : (lcsMatrix[i][index1] ? i : -1);
    return result;
}

private boolean[] getColumn(boolean[][] matrix, int colIndex) {
    boolean[] column = new boolean[matrix.length];
    for (int i = 0; i < matrix.length; i++) column[i] = matrix[i][colIndex];
    return column;
}

