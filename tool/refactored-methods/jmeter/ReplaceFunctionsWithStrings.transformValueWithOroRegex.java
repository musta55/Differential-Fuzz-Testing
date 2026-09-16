private JMeterProperty transformValueWithOroRegex(JMeterProperty prop) throws InvalidVariableException {
    PatternMatcher pm = JMeterUtils.getMatcher();
    PatternCompiler compiler = new Perl5Compiler();
    String input = prop.getStringValue();
    if (input == null) {
        return prop;
    }
    for (Map.Entry<String, String> entry : getVariables().entrySet()) {
        String key = entry.getKey();
        String value = entry.getValue();
        if (regexMatch) {
            try {
                Pattern pattern = compiler.compile(constructPattern(value));
                input = Util.substitute(pm, pattern, new StringSubstitution(FUNCTION_REF_PREFIX + key + FUNCTION_REF_SUFFIX), input, Util.SUBSTITUTE_ALL);
            } catch (MalformedPatternException e) {
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

