/**
 * Creates a UI for a JTextField.
 * <p>Note: this method is called by Swing.</p>
 *
 * @param component the text field
 * @return the UI
 */
@SuppressWarnings("unused")
public static ComponentUI createUI(JComponent component) {
    KerningOptimizer.INSTANCE.installKerningListener((JTextComponent) component);
    TextComponentUI.INSTANCE.installUndo((JTextComponent) component);
    if (component.getClass() == JTextArea.class) {
        component.addPropertyChangeListener("UI", evt -> component.setBorder(UIManager.getBorder(JMeterUIDefaults.TEXTAREA_BORDER)));
    }
    // Temporary restore the proper UI class
    UIManager.put(UI_CLASS, UIManager.get(BACKUP_UI_CLASS));
    try {
        return UIManager.getUI(component);
    } finally {
        // Add our class back so we handle the next created editor
        UIManager.put(UI_CLASS, TextAreaUIWithUndo.class.getName());
    }
}