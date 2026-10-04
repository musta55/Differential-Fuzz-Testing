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
    String ret = null;
    boolean success = true;
    Document testDoc = null;
    try {
        testDoc = XPathUtil.makeDocumentBuilder(false, false, false, false).newDocument();
        //$NON-NLS-1$
        Element el = testDoc.createElement("root");
        testDoc.appendChild(el);
        XPathUtil.validateXPath2(testDoc, xpathString, namespaces);
    } catch (IllegalArgumentException | ParserConfigurationException | TransformerException e) {
        log.warn("Exception while validating XPath.", e);
        success = false;
        ret = e.getLocalizedMessage();
    }
    if (showDialog) {
        JOptionPane.showMessageDialog(null, //$NON-NLS-1$
        success ? JMeterUtils.getResString("xpath_assertion_valid") : ret, //$NON-NLS-1$
        success ? //$NON-NLS-1$
        JMeterUtils.getResString("xpath_assertion_valid") : //$NON-NLS-1$
        JMeterUtils.getResString("xpath_assertion_failed"), //$NON-NLS-1$
        success ? //$NON-NLS-1$
        JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
    }
    return success;
}