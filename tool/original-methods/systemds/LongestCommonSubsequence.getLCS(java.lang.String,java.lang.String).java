public ArrayList<String> getLCS(String str1, String str2) {
    int m = str1.length();
    int n = str2.length();
    boolean[][] lcsMatrix = new boolean[m][n];
    ArrayList<String> allLCS = new ArrayList<>();
    for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) lcsMatrix[i][j] = false;
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            if ((str1.charAt(i) + "").equals(Lop.OPERAND_DELIMITOR) || (str2.charAt(j) + "").equals(Lop.OPERAND_DELIMITOR)) {
                continue;
            }
            if (str1.charAt(i) == str2.charAt(j)) {
                lcsMatrix[i][j] = true;
            }
        }
    }
    // layout 1: row-col
    for (int i = 0; i < m; i++) {
        if ((str1.charAt(i) + "").equals(Lop.OPERAND_DELIMITOR) || getCardinalityOfRow(lcsMatrix, i) == 0)
            continue;
        else {
            for (Integer j : getAllValuesOfRow(lcsMatrix, i)) {
                int li = i - 1;
                int lj = j - 1;
                if (li > 0 && lj > 0 && lcsMatrix[li][lj])
                    continue;
                StringBuilder sb = new StringBuilder();
                sb.append(str1.charAt(i));
                int l = j + 1;
                for (int k = i + 1; k < m && l < n; k++) {
                    int ul = getNextSetCol(lcsMatrix, k, l);
                    if (getCardinalityOfRow(lcsMatrix, k) == 0 || ul == -1) {
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
                    sb.append(str2.charAt(ul));
                    l = ul + 1;
                }
                if (sb.length() > 0 && !sb.toString().equals(Lop.OPERAND_DELIMITOR)) {
                    allLCS.add(sb.toString());
                }
            }
        }
    }
    /////////////////////////////
    // layout 2: col-row
    for (int j = 0; j < n; j++) {
        if ((str2.charAt(j) + "").equals(Lop.OPERAND_DELIMITOR) || getCardinalityOfCol(lcsMatrix, j) == 0)
            continue;
        else {
            for (Integer i : getAllValuesOfCol(lcsMatrix, j)) {
                int li = i - 1;
                int lj = j - 1;
                if (li > 0 && lj > 0 && lcsMatrix[li][lj])
                    continue;
                StringBuilder sb = new StringBuilder();
                sb.append(str2.charAt(j));
                int l = i + 1;
                for (int k = j + 1; k < n && l < m; k++) {
                    int ul = getNextSetRow(lcsMatrix, l, k);
                    if (getCardinalityOfCol(lcsMatrix, k) == 0 || ul == -1) {
                        if (!sb.toString().endsWith(Lop.OPERAND_DELIMITOR))
                            sb.append(Lop.OPERAND_DELIMITOR);
                        continue;
                    }
                    int lul = ul - 1;
                    int lk = k - 1;
                    if (ul - l > 0 || (lul > 0 && lk > 0 && !lcsMatrix[lul][lk])) {
                        if (!sb.toString().endsWith(Lop.OPERAND_DELIMITOR))
                            sb.append(Lop.OPERAND_DELIMITOR);
                    }
                    sb.append(str1.charAt(ul));
                    l = ul + 1;
                }
                if (sb.length() > 0 && !sb.toString().equals(Lop.OPERAND_DELIMITOR)) {
                    allLCS.add(sb.toString());
                }
            }
        }
    }
    return allLCS;
}