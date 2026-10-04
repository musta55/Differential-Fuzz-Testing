private static char getCharClass(char c) {
    Map<Character, Character> charClassMap = new HashMap<>();
    charClassMap.put(DIGIT, DIGIT);
    charClassMap.put(LOWER, LOWER);
    charClassMap.put(UPPER, UPPER);
    charClassMap.put(SPACE, SPACE);
    charClassMap.put(DOT, DOT);
    charClassMap.put(MINUS, MINUS);
    if (Character.isDigit(c))
        return DIGIT;
    if (Character.isLowerCase(c))
        return LOWER;
    if (Character.isUpperCase(c))
        return UPPER;
    if (Character.isSpaceChar(c))
        return SPACE;
    if (c == '.')
        return DOT;
    if (c == '-')
        return MINUS;
    return OTHER;
}