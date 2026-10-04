public static Map<String, Integer> LevelsExecutor(Map<String, Integer> old_pattern_hist, LEVEL_ENUM level) {
    Map<String, Integer> new_pattern_hist = new HashMap<>();
    for (Entry<String, Integer> e : old_pattern_hist.entrySet()) {
        String pattern = e.getKey();
        Integer nr_of_occurrences = e.getValue();
        String new_pattern;
        switch(level) {
            case // default encoding
            LEVEL1:
                new_pattern = encodeRawString(pattern);
                break;
            case // ignores the number of occurrences. It replaces all numbers with '+'
            LEVEL2:
                new_pattern = removeNumbers(pattern);
                break;
            case // ignores upper and lowercase characters. It replaces all 'u' and 'l' with 'a' = Alphabet
            LEVEL3:
                new_pattern = removeUpperLowerCase(pattern);
                break;
            case // changes floats to digits
            LEVEL4:
                new_pattern = removeInnerCharacterInPattern(pattern, DIGIT, DOT);
                break;
            case // removes spaces between strings
            LEVEL5:
                new_pattern = removeInnerCharacterInPattern(pattern, ALPHA, SPACE);
                break;
            case // changes negative numbers to digits
            LEVEL6:
                new_pattern = acceptNegativeNumbersAsDigits(pattern);
                break;
            default:
                new_pattern = "";
                break;
        }
        addDistinctValueOrIncrementCounter(new_pattern_hist, new_pattern, nr_of_occurrences);
    }
    return new_pattern_hist;
}