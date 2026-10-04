@Override
public void focusLost(FocusEvent e) {
    Component source = (Component) e.getSource();
    String text = "";
    if (source instanceof JTextComponent) {
        text = ((JTextComponent) source).getText();
    } else if (source instanceof TextComponent) {
        text = ((TextComponent) source).getText();
    }
    try {
        Integer.parseInt(text);
    } catch (NumberFormatException nfe) {
        JOptionPane.showMessageDialog(source, //$NON-NLS-1$
        JMeterUtils.getResString("you_must_enter_a_valid_number"), //$NON-NLS-1$
        JMeterUtils.getResString("invalid_data"), JOptionPane.WARNING_MESSAGE);
        FocusRequester.requestFocus(source);
    }
}