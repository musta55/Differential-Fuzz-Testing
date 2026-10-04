/**
 * Configures {@link UIDefaults} to use the patched class for {@code TextFieldUI}.
 *
 * @param defaults look and feel defaults
 */
public static void install(UIDefaults defaults) {
    Object lafUI = defaults.get(UI_CLASS);
    String newUI = TextAreaUIWithUndo.class.getName();
    if (!newUI.equals(lafUI)) {
        defaults.put(BACKUP_UI_CLASS, lafUI);
        defaults.put(UI_CLASS, newUI);
    }
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

