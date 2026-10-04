@Override
public void doAction(ActionEvent e) {
    if (ActionNames.SAVE_BEFORE_RUN.equals(e.getActionCommand())) {
        toggleSaveBeforeRunPreference();
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static void toggleSaveBeforeRunPreference() {
    GuiPackage guiInstance = GuiPackage.getInstance();
    boolean togglePreferenceValue = !guiInstance.shouldSaveBeforeRunByPreference();
    guiInstance.setSaveBeforeRunByPreference(togglePreferenceValue);
    guiInstance.getMenuItemSaveBeforeRunPanel().getModel().setSelected(togglePreferenceValue);
}

