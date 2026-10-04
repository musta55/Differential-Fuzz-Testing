/**
 * Test whether an XPath is valid. It seems the Xalan has no easy way to
 * check, so this creates a dummy test document, then tries to evaluate the xpath against it.
 *
 * @param xpathString
 *            XPath String to validate
 * @param showDialog
 *            weather to show a dialog
 * @param namespaces Namespaces declaration (set of prefix=value separated by spaces)
 * @return returns true if valid, false otherwise.
 */
public static boolean validXPath(String xpathString, boolean showDialog, String namespaces) {
    boolean success = true;
    try {
        Document testDoc = XPathUtil.makeDocumentBuilder(false, false, false, false).newDocument();
        //$NON-NLS-1$
        Element el = testDoc.createElement("root");
        testDoc.appendChild(el);
        XPathUtil.validateXPath2(testDoc, xpathString, namespaces);
    } catch (IllegalArgumentException | ParserConfigurationException | TransformerException e) {
        log.warn("Exception while validating XPath.", e);
        success = false;
        if (showDialog) {
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), JMeterUtils.getResString("xpath_assertion_failed"), JOptionPane.ERROR_MESSAGE);
        }
    }
    if (showDialog && success) {
        JOptionPane.showMessageDialog(null, JMeterUtils.getResString("xpath_assertion_valid"), JMeterUtils.getResString("xpath_assertion_valid"), JOptionPane.INFORMATION_MESSAGE);
    }
    return success;
}