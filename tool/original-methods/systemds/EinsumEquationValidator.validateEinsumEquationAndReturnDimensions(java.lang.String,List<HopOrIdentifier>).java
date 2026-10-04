public static <HopOrIdentifier extends ParseInfo> Triple<Long, Long, Types.DataType> validateEinsumEquationAndReturnDimensions(String equationString, List<HopOrIdentifier> expressionsOrIdentifiers) throws LanguageException {
    // length 2 if "...->..." , length 1 if "...->"
    String[] eqStringParts = equationString.split("->");
    boolean isResultScalar = eqStringParts.length == 1;
    if (expressionsOrIdentifiers == null)
        throw new RuntimeException("Einsum: called validateEinsumAndReturnDimensions with null list");
    HashMap<Character, Long> charToDimensionSize = new HashMap<>();
    Iterator<HopOrIdentifier> it = expressionsOrIdentifiers.iterator();
    HopOrIdentifier currArr = it.next();
    int arrSizeIterator = 0;
    int numberOfMatrices = 1;
    for (int i = 0; i < eqStringParts[0].length(); i++) {
        char c = equationString.charAt(i);
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
    if (expressionsOrIdentifiers.size() - 1 > numberOfMatrices)
        throw new LanguageException("Einsum: Provided more operands than specified in equation str");
    if (isResultScalar)
        return Triple.of(-1l, -1l, Types.DataType.SCALAR);
    int numberOfOutDimensions = 0;
    Character dim1Char = null;
    long dim1 = 1;
    long dim2 = 1;
    for (int i = 0; i < eqStringParts[1].length(); i++) {
        char c = eqStringParts[1].charAt(i);
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