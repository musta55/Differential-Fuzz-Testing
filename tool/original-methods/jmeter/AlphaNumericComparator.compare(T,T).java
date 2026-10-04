@Override
public int compare(T o1, T o2) {
    Matcher m1 = parts.matcher(converter.apply(o1));
    Matcher m2 = parts.matcher(converter.apply(o2));
    while (m1.find() && m2.find()) {
        int compareCharGroup = m1.group(ALPHA_PART).compareTo(m2.group(ALPHA_PART));
        if (compareCharGroup != 0) {
            return compareCharGroup;
        }
        String numberPart1 = m1.group(NUM_PART);
        String numberPart2 = m2.group(NUM_PART);
        if (numberPart1.isEmpty() || numberPart2.isEmpty()) {
            return compareOneEmptyPart(numberPart1, numberPart2);
        }
        String nonZeroNumberPart1 = trimLeadingZeroes(numberPart1);
        String nonZeroNumberPart2 = trimLeadingZeroes(numberPart2);
        int lengthNumber1 = nonZeroNumberPart1.length();
        int lengthNumber2 = nonZeroNumberPart2.length();
        if (lengthNumber1 != lengthNumber2) {
            if (lengthNumber1 < lengthNumber2) {
                return -1;
            }
            return 1;
        }
        int compareNumber = nonZeroNumberPart1.compareTo(nonZeroNumberPart2);
        if (compareNumber != 0) {
            return compareNumber;
        }
    }
    if (m1.hitEnd() && m2.hitEnd()) {
        return 0;
    }
    if (m1.hitEnd()) {
        return -1;
    }
    return 1;
}