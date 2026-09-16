/**
 * Check XPath button
 *
 * @return JButton
 */
public JButton getCheckXPathButton() {
    if (checkXPath == null) {
        //$NON-NLS-1$
        checkXPath = new JButton(JMeterUtils.getResString("xpath_assertion_button"));
        checkXPath.addActionListener(e -> validXPath(xpath.getText(), true));
    }
    return checkXPath;
}