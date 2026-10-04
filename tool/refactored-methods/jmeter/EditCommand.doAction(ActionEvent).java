@Override
public void doAction(ActionEvent e) {
    GuiPackage guiPackage = GuiPackage.getInstance();
    JMeterGUIComponent currentGui = guiPackage.getCurrentGui();
    updateMainPanel(guiPackage, currentGui);
    updateEditMenu(guiPackage);
    resetTextComponentUI();
    enableFileOperations(guiPackage);
}
// ---- helper method(s) introduced by the refactoring ----
private static void updateMainPanel(GuiPackage guiPackage, JMeterGUIComponent currentGui) {
    guiPackage.getMainFrame().setMainPanel((javax.swing.JComponent) currentGui);
}

private static void updateEditMenu(GuiPackage guiPackage) {
    guiPackage.getMainFrame().setEditMenu(guiPackage.getTreeListener().getCurrentNode().createPopupMenu());
}

private static void resetTextComponentUI() {
    TextComponentUI.INSTANCE.resetUndoHistory();
}

private static void enableFileOperations(GuiPackage guiPackage) {
    guiPackage.getMainFrame().setFileLoadEnabled(true);
    guiPackage.getMainFrame().setFileSaveEnabled(true);
}

