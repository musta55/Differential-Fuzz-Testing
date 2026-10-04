/**
 * Modifies a given TestElement to mirror the data in the gui components.
 *
 * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
 */
@Override
public void modifyTestElement(TestElement inElement) {
    log.debug("HTMLAssertionGui.modifyTestElement() called");
    configureTestElement(inElement);
    HTMLAssertion assertion = (HTMLAssertion) inElement;
    assertion.setErrorThreshold(parseLong(errorThresholdField.getText(), 0));
    assertion.setWarningThreshold(parseLong(warningThresholdField.getText(), 0));
    assertion.setDoctype(docTypeBox.getSelectedItem().toString());
    assertion.setErrorsOnly(errorsOnly.isSelected());
    if (htmlRadioButton.isSelected()) {
        assertion.setHTML();
    } else if (xhtmlRadioButton.isSelected()) {
        assertion.setXHTML();
    } else {
        assertion.setXML();
    }
    assertion.setFilename(filePanel.getFilename());
}
// ---- helper method(s) introduced by the refactoring ----
private static long parseLong(String value, long defaultValue) {
    try {
        return Long.parseLong(value);
    } catch (NumberFormatException e) {
        return defaultValue;
    }
}

private void toggleWarningThresholdField(boolean enabled) {
    warningThresholdField.setEnabled(enabled);
    warningThresholdField.setEditable(enabled);
}

