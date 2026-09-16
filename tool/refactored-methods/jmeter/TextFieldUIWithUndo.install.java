/**
 * Configures {@link UIDefaults} to use the patched class for {@code TextFieldUI}.
 * @param defaults look and feel defaults
 */
public static void install(UIDefaults defaults) {
    Object lafUI = defaults.get(UI_CLASS);
    String newUI = TextFieldUIWithUndo.class.getName();
    if (newUI.equals(lafUI)) {
        // Do not install the hook twice
        return;
    }
    backupOriginalUI(defaults, lafUI);
    updateUIDefaultsWithNewUI(defaults, newUI);
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

