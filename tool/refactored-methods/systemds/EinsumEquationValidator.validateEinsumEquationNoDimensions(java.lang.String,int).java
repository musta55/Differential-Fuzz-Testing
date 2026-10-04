public static Types.DataType validateEinsumEquationNoDimensions(String equationString, int numberOfMatrixInputs) throws LanguageException {
    String[] eqStringParts = equationString.split("->");
    boolean isResultScalar = eqStringParts.length == 1;
    int numberOfMatrices = countMatricesInInputPart(eqStringParts[0]);
    if (numberOfMatrixInputs != numberOfMatrices) {
        throw new LanguageException("Einsum: Invalid number of parameters, given: " + numberOfMatrixInputs + ", expected: " + numberOfMatrices);
    }
    if (isResultScalar) {
        return Types.DataType.SCALAR;
    } else {
        return parseOutputPartNoDimensions(eqStringParts[1]);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static <HopOrIdentifier extends ParseInfo> void parseInputPart(String inputPart, Iterator<HopOrIdentifier> it, HopOrIdentifier currArr, int arrSizeIterator, int numberOfMatrices, HashMap<Character, Long> charToDimensionSize) throws LanguageException {
    for (int i = 0; i < inputPart.length(); i++) {
        char c = inputPart.charAt(i);
        if (c == ' ')
            continue;
        if (c == ',') {
            if (!it.hasNext())
                throw new LanguageException("Einsum: Provided less operands than specified in equation str");
            currArr = it.next();
            arrSizeIterator = 0;
            numberOfMatrices++;
        } else {
            long thisCharDimension = getThisCharDimension(currArr, arrSizeIterator);
            if (charToDimensionSize.containsKey(c)) {
                if (charToDimensionSize.get(c) != thisCharDimension)
                    throw new LanguageException("Einsum: Character '" + c + "' expected to be dim " + charToDimensionSize.get(c) + ", but found " + thisCharDimension);
            } else {
                charToDimensionSize.put(c, thisCharDimension);
            }
            arrSizeIterator++;
        }
    }
}

private static Triple<Long, Long, Types.DataType> parseOutputPart(String outputPart, HashMap<Character, Long> charToDimensionSize) throws LanguageException {
    int numberOfOutDimensions = 0;
    Character dim1Char = null;
    long dim1 = 1;
    long dim2 = 1;
    for (int i = 0; i < outputPart.length(); i++) {
        char c = outputPart.charAt(i);
        if (c == ' ')
            continue;
        if (numberOfOutDimensions == 0) {
            dim1Char = c;
            if (!charToDimensionSize.containsKey(c))
                throw new LanguageException("Einsum: Output dimension '" + c + "' not present in input operands");
            dim1 = charToDimensionSize.get(c);
        } else {
            if (c == dim1Char)
                throw new LanguageException("Einsum: output character " + c + " provided multiple times");
            if (!charToDimensionSize.containsKey(c))
                throw new LanguageException("Einsum: Output dimension '" + c + "' not present in input operands");
            dim2 = charToDimensionSize.get(c);
        }
        numberOfOutDimensions++;
    }
    if (numberOfOutDimensions > 2) {
        throw new LanguageException("Einsum: output matrices with with no. dims > 2 not supported");
    } else {
        return Triple.of(dim1, dim2, Types.DataType.MATRIX);
    }
}

private static Types.DataType parseOutputPartNoDimensions(String outputPart) throws LanguageException {
    int numberOfDimensions = 0;
    Character dim1Char = null;
    for (int i = 0; i < outputPart.length(); i++) {
        char c = outputPart.charAt(i);
        if (c == ' ')
            continue;
        numberOfDimensions++;
        if (numberOfDimensions == 1 && c == dim1Char)
            throw new LanguageException("Einsum: output character " + c + " provided multiple times");
        dim1Char = c;
    }
    if (numberOfDimensions > 2) {
        throw new LanguageException("Einsum: output matrices with with no. dims > 2 not supported");
    } else {
        return Types.DataType.MATRIX;
    }
}

private static int countMatricesInInputPart(String inputPart) {
    int numberOfMatrices = 1;
    for (int i = 0; i < inputPart.length(); i++) {
        char c = inputPart.charAt(i);
        if (c == ' ')
            continue;
        if (c == ',')
            numberOfMatrices++;
    }
    return numberOfMatrices;
}

