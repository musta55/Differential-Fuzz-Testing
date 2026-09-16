/**
 * Configures the associated test element.
 * {@inheritDoc}
 */
@Override
public void configure(TestElement inElement) {
    super.configure(inElement);
    HTMLAssertion lAssertion = (HTMLAssertion) inElement;
    errorThresholdField.setText(String.valueOf(lAssertion.getErrorThreshold()));
    warningThresholdField.setText(String.valueOf(lAssertion.getWarningThreshold()));
    errorsOnly.setSelected(lAssertion.isErrorsOnly());
    docTypeBox.setSelectedItem(lAssertion.getDoctype());
    if (lAssertion.isHTML()) {
        htmlRadioButton.setSelected(true);
    } else if (lAssertion.isXHTML()) {
        xhtmlRadioButton.setSelected(true);
    } else {
        xmlRadioButton.setSelected(true);
    }
    if (lAssertion.isErrorsOnly()) {
        warningThresholdField.setEnabled(false);
        warningThresholdField.setEditable(false);
    } else {
        warningThresholdField.setEnabled(true);
        warningThresholdField.setEditable(true);
    }
    filePanel.setFilename(lAssertion.getFilename());
}