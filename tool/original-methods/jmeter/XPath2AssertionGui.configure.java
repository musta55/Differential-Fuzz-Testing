@Override
public void configure(TestElement el) {
    super.configure(el);
    XPath2Assertion assertion = (XPath2Assertion) el;
    showScopeSettings(assertion, true);
    xpath.setXPath(assertion.getXPathString());
    xpath.setNegated(assertion.isNegated());
    xpath.setNamespaces(assertion.getNamespaces());
}