/**
 * Check XPath button
 *
 * @return JButton
 */
public JButton getCheckXPathButton() {
    if (checkXPath == null) {
        //$NON-NLS-1$
        checkXPath = new JButton(JMeterUtils.getResString("xpath_assertion_button"));
        checkXPath.addActionListener(e -> {
            boolean isValid = validXPath(xpath.getText());
            showValidationDialog(isValid, isValid ? null : getValidationErrorMessage());
        });
    }
    return checkXPath;
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Test whether an XPath is valid.
 *
 * @param xpathString
 *            XPath String to validate
 * @return returns true if valid, false otherwise.
 */
public static boolean validXPath(String xpathString) {
    try {
        Document testDoc = XPathUtil.makeDocumentBuilder(false, false, false, false).newDocument();
        //$NON-NLS-1$
        Element el = testDoc.createElement("root");
        testDoc.appendChild(el);
        XPathUtil.validateXPath(testDoc, xpathString);
        return true;
    } catch (IllegalArgumentException | ParserConfigurationException | TransformerException e) {
        log.warn("Exception while validating XPath.", e);
        return false;
    }
}

private static void showValidationDialog(boolean isValid, String errorMessage) {
    JOptionPane.showMessageDialog(null, //$NON-NLS-1$
    isValid ? JMeterUtils.getResString("xpath_assertion_valid") : errorMessage, //$NON-NLS-1$
    isValid ? //$NON-NLS-1$
    JMeterUtils.getResString("xpath_assertion_valid") : //$NON-NLS-1$
    JMeterUtils.getResString("xpath_assertion_failed"), isValid ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
}

private static String getValidationErrorMessage() {
    //$NON-NLS-1$
    return JMeterUtils.getResString("xpath_assertion_failed");
}

