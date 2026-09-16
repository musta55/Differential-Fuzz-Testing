/**
 * Creates a UI for a JTextField.
 * <p>Note: this method is called by Swing.</p>
 *
 * @param component the text field
 * @return the UI
 */
@SuppressWarnings("unused")
public static ComponentUI createUI(JComponent component) {
    installKerningAndUndo((JTextComponent) component);
    configureTextAreaBorder(component);
    return getRestoredUI(component);
}
// ---- helper method(s) introduced by the refactoring ----
private static void installKerningAndUndo(JTextComponent textComponent) {
    KerningOptimizer.INSTANCE.installKerningListener(textComponent);
    TextComponentUI.INSTANCE.installUndo(textComponent);
}

private static void configureTextAreaBorder(JComponent component) {
    if (component instanceof JTextArea) {
        component.addPropertyChangeListener("UI", evt -> component.setBorder(UIManager.getBorder(JMeterUIDefaults.TEXTAREA_BORDER)));
    }
}

private static ComponentUI getRestoredUI(JComponent component) {
    String originalUI = (String) UIManager.get(BACKUP_UI_CLASS);
    UIManager.put(UI_CLASS, originalUI);
    try {
        return UIManager.getUI(component);
    } finally {
        UIManager.put(UI_CLASS, TextAreaUIWithUndo.class.getName());
    }
}

