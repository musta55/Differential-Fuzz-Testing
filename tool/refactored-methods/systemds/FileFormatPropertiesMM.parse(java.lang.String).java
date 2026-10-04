public static FileFormatPropertiesMM parse(String header) {
    //example: %%MatrixMarket matrix coordinate real general
    //(note: we use a string tokenizer because the individual
    //components can be separated by an arbitrary number of spaces)
    StringTokenizer st = new StringTokenizer(header, " ");
    //check basic structure and matrix object
    expectTokenCount(st, 5);
    expectToken(st, "%%MatrixMarket");
    expectToken(st, "matrix");
    //check format, field, and
    MMFormat fmt = parseFormat(st.nextToken());
    MMField f = parseField(st.nextToken());
    MMSymmetry s = parseSymmetry(st.nextToken());
    //construct file properties and check valid combination
    return new FileFormatPropertiesMM(fmt, f, s);
}
// ---- helper method(s) introduced by the refactoring ----
private static void expectTokenCount(StringTokenizer st, int expected) {
    int numTokens = st.countTokens();
    if (numTokens != expected) {
        throw new DMLRuntimeException("MatrixMarket: Incorrect number of header tokens: " + numTokens + " (expected: " + expected + ").");
    }
}

private static void expectToken(StringTokenizer st, String expected) {
    String token = st.nextToken();
    if (!token.equals(expected)) {
        throw new DMLRuntimeException("MatrixMarket: Incorrect header component: " + token + " (expected: " + expected + ").");
    }
}

private static MMFormat parseFormat(String format) {
    switch(format) {
        case "coordinate":
            return MMFormat.COORDINATE;
        default:
            throw new DMLRuntimeException("MatrixMarket: Incorrect format: " + format + " (expected coordinate).");
    }
}

private static MMField parseField(String field) {
    switch(field) {
        case "real":
            return MMField.REAL;
        case "integer":
            return MMField.INTEGER;
        case "pattern":
            return MMField.PATTERN;
        default:
            throw new DMLRuntimeException("MatrixMarket: Incorrect field: " + field + " (expected real | integer | pattern).");
    }
}

private static MMSymmetry parseSymmetry(String symmetry) {
    switch(symmetry) {
        case "general":
            return MMSymmetry.GENERAL;
        case "symmetric":
            return MMSymmetry.SYMMETRIC;
        default:
            throw new DMLRuntimeException("MatrixMarket: Incorrect symmetry: " + symmetry + " (expected general | symmetric).");
    }
}

