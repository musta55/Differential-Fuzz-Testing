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
        assertion.setProperty(DurationAssertion.DURATION_KEY, duration.getText());
        saveScopeSettings(assertion);
    }
}