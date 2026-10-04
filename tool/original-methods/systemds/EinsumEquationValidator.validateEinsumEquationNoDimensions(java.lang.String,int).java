public static Types.DataType validateEinsumEquationNoDimensions(String equationString, int numberOfMatrixInputs) throws LanguageException {
    // length 2 if "...->..." , length 1 if "...->"
    String[] eqStringParts = equationString.split("->");
    boolean isResultScalar = eqStringParts.length == 1;
    int numberOfMatrices = 1;
    for (int i = 0; i < eqStringParts[0].length(); i++) {
        char c = eqStringParts[0].charAt(i);
        if (c == ' ')
            continue;
        if (c == ',')
            numberOfMatrices++;
    }
    if (numberOfMatrixInputs != numberOfMatrices) {
        throw new LanguageException("Einsum: Invalid number of parameters, given: " + numberOfMatrixInputs + ", expected: " + numberOfMatrices);
    }
    if (isResultScalar) {
        return Types.DataType.SCALAR;
    } else {
        int numberOfDimensions = 0;
        Character dim1Char = null;
        for (int i = 0; i < eqStringParts[1].length(); i++) {
            char c = eqStringParts[i].charAt(i);
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
}