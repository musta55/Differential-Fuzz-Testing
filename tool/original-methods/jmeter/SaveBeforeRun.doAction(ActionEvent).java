@Override
public void doAction(ActionEvent e) {
    if (ActionNames.SAVE_BEFORE_RUN.equals(e.getActionCommand())) {
        // toggle boolean preference value
        GuiPackage guiInstance = GuiPackage.getInstance();
        boolean togglePreferenceValue = !guiInstance.shouldSaveBeforeRunByPreference();
        guiInstance.setSaveBeforeRunByPreference(togglePreferenceValue);
        // toggle check box
        guiInstance.getMenuItemSaveBeforeRunPanel().getModel().setSelected(togglePreferenceValue);
    }
}