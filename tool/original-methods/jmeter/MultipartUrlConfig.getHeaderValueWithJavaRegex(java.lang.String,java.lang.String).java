private static String getHeaderValueWithJavaRegex(String multiPart, String regularExpression) {
    java.util.regex.Pattern pattern = JMeterUtils.compilePattern(regularExpression, java.util.regex.Pattern.CASE_INSENSITIVE | java.util.regex.Pattern.MULTILINE);
    Matcher matcher = pattern.matcher(multiPart);
    if (matcher.find()) {
        return matcher.group(1).trim();
    }
    return null;
}