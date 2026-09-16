@Override
public void configure(TestElement el) {
    super.configure(el);
    XPath2Assertion assertion = (XPath2Assertion) el;
    showScopeSettings(assertion, true);
    setXPathPanelValues(assertion);
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

