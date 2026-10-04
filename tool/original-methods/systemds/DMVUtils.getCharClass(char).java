private static char getCharClass(char c) {
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