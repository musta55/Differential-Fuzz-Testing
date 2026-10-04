public static EinsumContext getEinsumContext(String eqStr, List<MatrixBlock> inputs) {
    EinsumContext res = new EinsumContext();
    res.equationString = eqStr;
    res.characterAppearanceCount = new HashMap<>();
    res.charToDimensionSize = new HashMap<>();
    res.newEquationStringInputsSplit = splitEquationString(eqStr, res.characterAppearanceCount);
    res.validateDimensions(eqStr, inputs, res.newEquationStringInputsSplit, res.charToDimensionSize);
    res.setOutputDimensions(res.newEquationStringInputsSplit, res.charToDimensionSize);
    return res;
}
// ---- helper method(s) introduced by the refactoring ----
private static List<String> splitEquationString(String eqStr, Map<Character, Integer> characterAppearanceCount) {
    List<String> newEquationStringSplit = new ArrayList<>();
    StringBuilder sb = new StringBuilder(2);
    for (int i = 0; i < eqStr.length(); i++) {
        char c = eqStr.charAt(i);
        if (c == ' ')
            continue;
        if (c == ',' || c == '-')
            break;
        if (!Character.isAlphabetic(c)) {
            throw new RuntimeException("Einsum: only alphabetic characters are supported for dimensions: " + c);
        }
        sb.append(c);
        characterAppearanceCount.put(c, characterAppearanceCount.getOrDefault(c, 0) + 1);
    }
    newEquationStringSplit.add(sb.toString());
    return newEquationStringSplit;
}

private void validateDimensions(String eqStr, List<MatrixBlock> inputs, List<String> newEquationStringSplit, Map<Character, Integer> charToDimensionSize) {
    Iterator<MatrixBlock> it = inputs.iterator();
    MatrixBlock curArr = it.next();
    int i = 0;
    for (String s : newEquationStringSplit) {
        if (s.length() > 0) {
            setDimension(s.charAt(0), curArr.getNumRows(), charToDimensionSize);
        }
        if (s.length() > 1) {
            setDimension(s.charAt(1), curArr.getNumColumns(), charToDimensionSize);
        }
        if (s.length() > 2) {
            throw new RuntimeException("Einsum: only up-to 2D inputs strings allowed ");
        }
        if (it.hasNext()) {
            curArr = it.next();
        }
    }
    if (i == eqStr.length() - 1) {
        throw new RuntimeException("Einsum: missing '->' substring " + eqStr.charAt(i));
    }
    if (i == eqStr.length() - 1 || eqStr.charAt(i + 1) != '>') {
        throw new RuntimeException("Einsum: missing '->' substring " + eqStr.charAt(i));
    }
}

private void setDimension(char c, int size, Map<Character, Integer> charToDimensionSize) {
    if (charToDimensionSize.containsKey(c) && charToDimensionSize.get(c) != size) {
        throw new RuntimeException("Einsum: character " + c + " has multiple conflicting sizes");
    }
    charToDimensionSize.put(c, size);
}

private void setOutputDimensions(List<String> newEquationStringSplit, Map<Character, Integer> charToDimensionSize) {
    StringBuilder sb = new StringBuilder(2);
    int i = equationString.indexOf("->") + 2;
    for (; i < equationString.length(); i++) {
        char c = equationString.charAt(i);
        if (c == ' ')
            continue;
        if (!Character.isAlphabetic(c)) {
            throw new RuntimeException("Einsum: only alphabetic characters are supported for dimensions: " + c);
        }
        sb.append(c);
    }
    String s = sb.toString();
    if (s.length() > 0)
        outChar1 = s.charAt(0);
    if (s.length() > 1)
        outChar2 = s.charAt(1);
    if (s.length() > 2) {
        throw new RuntimeException("Einsum: only up-to 2D output allowed ");
    }
    outRows = (outChar1 == null ? 1 : charToDimensionSize.get(outChar1));
    outCols = (outChar2 == null ? 1 : charToDimensionSize.get(outChar2));
}

