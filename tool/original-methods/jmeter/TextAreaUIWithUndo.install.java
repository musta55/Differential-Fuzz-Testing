/**
 * Configures {@link UIDefaults} to use the patched class for {@code TextFieldUI}.
 *
 * @param defaults look and feel defaults
 */
public static void install(UIDefaults defaults) {
    Object lafUI = defaults.get(UI_CLASS);
    String newUI = TextAreaUIWithUndo.class.getName();
    if (newUI.equals(lafUI)) {
        // Do not install the hook twice
        return;
    }
    defaults.put(BACKUP_UI_CLASS, lafUI);
    defaults.put(UI_CLASS, newUI);
}