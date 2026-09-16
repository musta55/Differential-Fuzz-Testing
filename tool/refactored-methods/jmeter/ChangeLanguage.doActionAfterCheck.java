/**
 * @see org.apache.jmeter.gui.action.AbstractActionWithNoRunningTest#doActionAfterCheck(ActionEvent)
 */
@Override
public void doActionAfterCheck(ActionEvent e) {
    String locale = ((Component) e.getSource()).getName();
    Locale loc = parseLocale(locale);
    log.debug("Changing locale to {}", loc);
    try {
        JMeterUtils.setLocale(loc);
    } catch (JMeterError err) {
        JMeterUtils.reportErrorToUser(err.toString());
    }
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Parses a locale string into a Locale object.
 *
 * @param localeString the locale string to parse
 * @return the parsed Locale object
 */
private static Locale parseLocale(String localeString) {
    int sep = localeString.indexOf('_');
    if (sep > 0) {
        return new Locale(localeString.substring(0, sep), localeString.substring(sep + 1));
    } else {
        return new Locale(localeString, "");
    }
}

