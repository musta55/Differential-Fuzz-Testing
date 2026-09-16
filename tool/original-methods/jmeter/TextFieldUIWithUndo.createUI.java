/**
 * Creates a UI for a JTextField.
 * <p>Note: this method is called by Swing.</p>
 *
 * @param c the text field
 * @return the UI
 */
@SuppressWarnings("unused")
public static ComponentUI createUI(JComponent c) {
    TextComponentUI.INSTANCE.installUndo((JTextComponent) c);
    // Temporary restore the proper UI class
    UIManager.put(UI_CLASS, UIManager.get(BACKUP_UI_CLASS));
    try {
        return UIManager.getUI(c);
    } finally {
        // Add our class back so we handle the next created editor
        UIManager.put(UI_CLASS, TextFieldUIWithUndo.class.getName());
    }
}