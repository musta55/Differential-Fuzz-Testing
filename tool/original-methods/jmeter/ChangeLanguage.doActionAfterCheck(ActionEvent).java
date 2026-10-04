/**
 * @see org.apache.jmeter.gui.action.AbstractActionWithNoRunningTest#doActionAfterCheck(ActionEvent)
 */
@Override
public void doActionAfterCheck(ActionEvent e) {
    String locale = ((Component) e.getSource()).getName();
    Locale loc;
    int sep = locale.indexOf('_');
    if (sep > 0) {
        loc = new Locale(locale.substring(0, sep), locale.substring(sep + 1));
    } else {
        loc = new Locale(locale, "");
    }
    log.debug("Changing locale to {}", loc);
    try {
        JMeterUtils.setLocale(loc);
    } catch (JMeterError err) {
        JMeterUtils.reportErrorToUser(err.toString());
    }
}