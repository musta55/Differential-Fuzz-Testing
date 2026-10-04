/**
 * Implements JMeterGUIComponent.clearGui
 */
@Override
public void clearGui() {
    super.clearGui();
    //$NON-NLS-1$
    xpath.setXPath("/");
    xpath.setNegated(false);
    xpath.setNamespaces("");
}