/**
 * Configures the associated test element.
 * {@inheritDoc}
 */
@Override
public void configure(TestElement inElement) {
    super.configure(inElement);
    HTMLAssertion assertion = (HTMLAssertion) inElement;
    errorThresholdField.setText(String.valueOf(assertion.getErrorThreshold()));
    warningThresholdField.setText(String.valueOf(assertion.getWarningThreshold()));
    errorsOnly.setSelected(assertion.isErrorsOnly());
    docTypeBox.setSelectedItem(assertion.getDoctype());
    if (assertion.isHTML()) {
        htmlRadioButton.setSelected(true);
    } else if (assertion.isXHTML()) {
        xhtmlRadioButton.setSelected(true);
    } else {
        xmlRadioButton.setSelected(true);
    }
    toggleWarningThresholdField(!errorsOnly.isSelected());
    filePanel.setFilename(assertion.getFilename());
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

