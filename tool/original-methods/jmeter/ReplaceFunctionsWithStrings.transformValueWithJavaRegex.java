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
            input = StringUtilities.substitute(input, value, FUNCTION_REF_PREFIX + key + FUNCTION_REF_SUFFIX);
        }
    }
    return new StringProperty(prop.getName(), input);
}