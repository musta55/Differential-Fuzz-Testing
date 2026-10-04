@Override
public int compare(T o1, T o2) {
    Matcher m1 = parts.matcher(converter.apply(o1));
    Matcher m2 = parts.matcher(converter.apply(o2));
    while (m1.find() && m2.find()) {
        int charGroupComparison = compareCharGroups(m1, m2);
        if (charGroupComparison != 0) {
            return charGroupComparison;
        }
        int numberPartComparison = compareNumberParts(m1, m2);
        if (numberPartComparison != 0) {
            return numberPartComparison;
        }
    }
    return compareRemainingParts(m1, m2);
}
// ---- helper method(s) introduced by the refactoring ----
private static int compareCharGroups(Matcher m1, Matcher m2) {
    return m1.group(ALPHA_PART).compareTo(m2.group(ALPHA_PART));
}

private static int compareNumberParts(Matcher m1, Matcher m2) {
    String numberPart1 = m1.group(NUM_PART);
    String numberPart2 = m2.group(NUM_PART);
    if (numberPart1.isEmpty() || numberPart2.isEmpty()) {
        return compareOneEmptyPart(numberPart1, numberPart2);
    }
    String nonZeroNumberPart1 = trimLeadingZeroes(numberPart1);
    String nonZeroNumberPart2 = trimLeadingZeroes(numberPart2);
    int lengthComparison = Integer.compare(nonZeroNumberPart1.length(), nonZeroNumberPart2.length());
    if (lengthComparison != 0) {
        return lengthComparison;
    }
    return nonZeroNumberPart1.compareTo(nonZeroNumberPart2);
}

private static int compareRemainingParts(Matcher m1, Matcher m2) {
    if (m1.hitEnd() && m2.hitEnd()) {
        return 0;
    }
    if (m1.hitEnd()) {
        return -1;
    }
    return 1;
}

