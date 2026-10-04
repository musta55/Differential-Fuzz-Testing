@Override
public JMeterProperty transformValue(JMeterProperty prop) throws InvalidVariableException {
    String input = prop.getStringValue();
    for (Map.Entry<String, String> entry : getVariables().entrySet()) {
        String key = entry.getKey();
        String value = entry.getValue();
        input = StringUtilities.substitute(input, "${" + key + "}", value);
    }
    return new StringProperty(prop.getName(), input);
}