/**
 * Modifies a given TestElement to mirror the data in the gui components.
 *
 * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
 */
@Override
public void modifyTestElement(TestElement el) {
    super.configureTestElement(el);
    if (el instanceof XPath2Assertion) {
        XPath2Assertion assertion = (XPath2Assertion) el;
        saveScopeSettings(assertion);
        updateAssertionFromPanel(assertion);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void setXPathPanelValues(XPath2Assertion assertion) {
    xpath.setXPath(assertion.getXPathString());
    xpath.setNegated(assertion.isNegated());
    xpath.setNamespaces(assertion.getNamespaces());
}

private void updateAssertionFromPanel(XPath2Assertion assertion) {
    assertion.setNegated(xpath.isNegated());
    assertion.setXPathString(xpath.getXPath());
    assertion.setNamespaces(xpath.getNamespaces());
}

private void resetXPathPanel() {
    //$NON-NLS-1$
    xpath.setXPath("/");
    xpath.setNegated(false);
    xpath.setNamespaces("");
}

