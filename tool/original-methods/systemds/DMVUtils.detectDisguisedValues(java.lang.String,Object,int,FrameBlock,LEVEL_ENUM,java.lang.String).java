private static void detectDisguisedValues(String dom_pattern, Object col, int col_idx, FrameBlock frameBlock, LEVEL_ENUM level, String disguisedVal) {
    int row_idx = -1;
    String pattern = "";
    String[] attr = (String[]) col;
    int numRows = frameBlock.getNumRows();
    for (int i = 0; i < numRows; i++) {
        String value = (attr[i] == null) ? "NULL" : attr[i];
        switch(level) {
            case LEVEL1:
                pattern = encodeRawString(value);
                break;
            case LEVEL2:
                pattern = encodeRawString(value);
                pattern = removeNumbers(pattern);
                break;
            case LEVEL3:
                pattern = encodeRawString(value);
                pattern = removeNumbers(pattern);
                pattern = removeUpperLowerCase(pattern);
                break;
            case LEVEL4:
                pattern = encodeRawString(value);
                pattern = removeNumbers(pattern);
                pattern = removeUpperLowerCase(pattern);
                pattern = removeInnerCharacterInPattern(pattern, DIGIT, DOT);
                break;
            case LEVEL5:
                pattern = encodeRawString(value);
                pattern = removeNumbers(pattern);
                pattern = removeUpperLowerCase(pattern);
                pattern = removeInnerCharacterInPattern(pattern, DIGIT, DOT);
                pattern = removeInnerCharacterInPattern(pattern, ALPHA, SPACE);
                break;
            case LEVEL6:
                pattern = encodeRawString(value);
                pattern = removeNumbers(pattern);
                pattern = removeUpperLowerCase(pattern);
                pattern = removeInnerCharacterInPattern(pattern, DIGIT, DOT);
                pattern = removeInnerCharacterInPattern(pattern, ALPHA, SPACE);
                pattern = acceptNegativeNumbersAsDigits(pattern);
                break;
            default:
                throw new DMLRuntimeException("Could not find suitable level");
        }
        row_idx++;
        if (pattern.equals(dom_pattern))
            continue;
        frameBlock.set(row_idx, col_idx, disguisedVal);
    }
}