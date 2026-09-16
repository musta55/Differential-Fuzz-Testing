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
        assertion.setNegated(xpath.isNegated());
        assertion.setXPathString(xpath.getXPath());
        assertion.setNamespaces(xpath.getNamespaces());
    }
}