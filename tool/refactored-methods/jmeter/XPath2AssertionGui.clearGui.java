/**
 * Implements JMeterGUIComponent.clearGui
 */
@Override
public void clearGui() {
    super.clearGui();
    resetXPathPanel();
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

