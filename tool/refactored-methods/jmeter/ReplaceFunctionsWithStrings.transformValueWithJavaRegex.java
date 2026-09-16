private JMeterProperty transformValueWithJavaRegex(JMeterProperty prop) throws InvalidVariableException {
    String input = prop.getStringValue();
    if (input == null) {
        return prop;
    }
    for (Map.Entry<String, String> entry : getVariables().entrySet()) {
        String key = entry.getKey();
        String value = entry.getValue();
        if (regexMatch) {
            try {
                java.util.regex.Pattern pattern = JMeterUtils.compilePattern(constructPattern(value));
                input = pattern.matcher(input).replaceAll(FUNCTION_REF_PREFIX_REGEX_SAFE + key + FUNCTION_REF_SUFFIX);
            } catch (PatternSyntaxException e) {
                log.warn("Malformed pattern: {}", value);
            }
        } else {
            input = substituteNonRegex(input, value, key);
        }
    }
    return new StringProperty(prop.getName(), input);
}
// ---- helper method(s) introduced by the refactoring ----
private static String substituteNonRegex(String input, String value, String key) {
    return StringUtilities.substitute(input, value, FUNCTION_REF_PREFIX + key + FUNCTION_REF_SUFFIX);
}

