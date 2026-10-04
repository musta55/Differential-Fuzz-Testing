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
    restoreOriginalUI();
    try {
        return UIManager.getUI(c);
    } finally {
        reinstallCustomUI();
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static void backupOriginalUI(UIDefaults defaults, Object lafUI) {
    defaults.put(BACKUP_UI_CLASS, lafUI);
}

private static void updateUIDefaultsWithNewUI(UIDefaults defaults, String newUI) {
    defaults.put(UI_CLASS, newUI);
}

private static void restoreOriginalUI() {
    UIManager.put(UI_CLASS, UIManager.get(BACKUP_UI_CLASS));
}

private static void reinstallCustomUI() {
    UIManager.put(UI_CLASS, TextFieldUIWithUndo.class.getName());
}

