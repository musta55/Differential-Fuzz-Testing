/**
 * Modifies a given TestElement to mirror the data in the gui components.
 *
 * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
 */
@Override
public void modifyTestElement(TestElement inElement) {
    log.debug("HTMLAssertionGui.modifyTestElement() called");
    configureTestElement(inElement);
    String errorThresholdString = errorThresholdField.getText();
    long errorThreshold = 0;
    try {
        errorThreshold = Long.parseLong(errorThresholdString);
    } catch (NumberFormatException e) {
        errorThreshold = 0;
    }
    ((HTMLAssertion) inElement).setErrorThreshold(errorThreshold);
    String warningThresholdString = warningThresholdField.getText();
    long warningThreshold = 0;
    try {
        warningThreshold = Long.parseLong(warningThresholdString);
    } catch (NumberFormatException e) {
        warningThreshold = 0;
    }
    ((HTMLAssertion) inElement).setWarningThreshold(warningThreshold);
    String docTypeString = docTypeBox.getSelectedItem().toString();
    ((HTMLAssertion) inElement).setDoctype(docTypeString);
    boolean trackErrorsOnly = errorsOnly.isSelected();
    ((HTMLAssertion) inElement).setErrorsOnly(trackErrorsOnly);
    if (htmlRadioButton.isSelected()) {
        ((HTMLAssertion) inElement).setHTML();
    } else if (xhtmlRadioButton.isSelected()) {
        ((HTMLAssertion) inElement).setXHTML();
    } else {
        ((HTMLAssertion) inElement).setXML();
    }
    ((HTMLAssertion) inElement).setFilename(filePanel.getFilename());
}