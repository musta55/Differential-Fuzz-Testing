private boolean isEquals(Object subj) {
    if (isUseRegex()) {
        String str = objectToString(subj);
        if (USE_JAVA_REGEX) {
            return JMeterUtils.compilePattern(getExpectedValue()).matcher(str).matches();
        } else {
            Pattern pattern = JMeterUtils.getPatternCache().getPattern(getExpectedValue());
            return JMeterUtils.getMatcher().matches(str, pattern);
        }
    } else {
        Object expected = JSONValue.parse(getExpectedValue());
        return Objects.equals(expected, subj);
    }
}