/**
 * @see org.apache.jmeter.functions.Function#execute
 */
@Override
public String toString() {
    JMeterVariables vars = getVariables();
    String value = vars != null ? vars.get(name) : null;
    return value != null ? value : constructReturnString();
}
// ---- helper method(s) introduced by the refactoring ----
private String constructReturnString() {
    return "${" + name + "}";
}

