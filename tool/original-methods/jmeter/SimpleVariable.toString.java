/**
 * @see org.apache.jmeter.functions.Function#execute
 */
@Override
public String toString() {
    String ret = null;
    JMeterVariables vars = getVariables();
    if (vars != null) {
        ret = vars.get(name);
    }
    if (ret == null) {
        return "${" + name + "}";
    }
    return ret;
}