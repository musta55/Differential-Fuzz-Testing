/**
 * Modifies a given TestElement to mirror the data in the gui components.
 *
 * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
 */
@Override
public void modifyTestElement(TestElement el) {
    configureTestElement(el);
    if (el instanceof DurationAssertion) {
        DurationAssertion assertion = (DurationAssertion) el;
        setDurationProperty(assertion);
        saveScopeSettings(assertion);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void setDurationProperty(DurationAssertion assertion) {
    assertion.setProperty(DurationAssertion.DURATION_KEY, duration.getText());
}

private void setDurationText(DurationAssertion da) {
    duration.setText(da.getPropertyAsString(DurationAssertion.DURATION_KEY));
}

