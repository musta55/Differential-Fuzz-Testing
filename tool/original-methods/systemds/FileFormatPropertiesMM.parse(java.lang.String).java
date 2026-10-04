public static FileFormatPropertiesMM parse(String header) {
    //example: %%MatrixMarket matrix coordinate real general
    //(note: we use a string tokenizer because the individual
    //components can be separated by an arbitrary number of spaces)
    StringTokenizer st = new StringTokenizer(header, " ");
    //check basic structure and matrix object
    int numTokens = st.countTokens();
    if (numTokens != 5)
        throw new DMLRuntimeException("MatrixMarket: Incorrect number of header tokens: " + numTokens + " (expeced: 5).");
    String type = st.nextToken();
    if (!type.equals("%%MatrixMarket"))
        throw new DMLRuntimeException("MatrixMarket: Incorrect header start: " + type + " (expected: %%MatrixMarket).");
    String object = st.nextToken();
    if (!object.equals("matrix"))
        throw new DMLRuntimeException("MatrixMarket: Incorrect object: " + object + " (expected: matrix).");
    //check format, field, and
    String format = st.nextToken();
    MMFormat fmt = null;
    switch(format) {
        //case "array": fmt = MMFormat.ARRAY; break;
        case "coordinate":
            fmt = MMFormat.COORDINATE;
            break;
        default:
            throw new DMLRuntimeException("MatrixMarket: " + "Incorrect format: " + format + " (expected coordinate).");
    }
    String field = st.nextToken();
    MMField f = null;
    switch(field) {
        case "real":
            f = MMField.REAL;
            break;
        case "integer":
            f = MMField.INTEGER;
            break;
        case "pattern":
            f = MMField.PATTERN;
            break;
        //note: complex not supported
        default:
            throw new DMLRuntimeException("MatrixMarket: " + "Incorrect field: " + field + " (expected real | integer | pattern).");
    }
    String symmetry = st.nextToken();
    MMSymmetry s = null;
    switch(symmetry) {
        case "general":
            s = MMSymmetry.GENERAL;
            break;
        case "symmetric":
            s = MMSymmetry.SYMMETRIC;
            break;
        //case "skew-symmetric": s = MMSymmetry.SKEW_SYMMETRIC; break; //not support in R
        //note: Hermitian not supported
        default:
            throw new DMLRuntimeException("MatrixMarket: " + "Incorrect symmetry: " + symmetry + " (expected general | symmetric).");
    }
    //construct file properties and check valid combination
    return new FileFormatPropertiesMM(fmt, f, s);
}