@Override
public JMeterProperty transformValue(JMeterProperty prop) throws InvalidVariableException {
    String input = prop.getStringValue();
    input = substituteVariables(input, getVariables());
    return new StringProperty(prop.getName(), input);
}
// ---- helper method(s) introduced by the refactoring ----
private static String substituteVariables(String input, Map<String, String> variables) {
    for (Map.Entry<String, String> entry : variables.entrySet()) {
        String key = entry.getKey();
        String value = entry.getValue();
        input = StringUtilities.substitute(input, "${" + key + "}", value);
    }
    return input;
}

