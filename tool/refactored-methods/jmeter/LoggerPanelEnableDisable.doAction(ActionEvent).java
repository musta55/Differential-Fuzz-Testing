/**
 * This method performs the actual command processing.
 *
 * @param e the generic UI action event
 */
@Override
public void doAction(ActionEvent e) {
    if (ActionNames.LOGGER_PANEL_ENABLE_DISABLE.equals(e.getActionCommand())) {
        GuiPackage guiInstance = GuiPackage.getInstance();
        JSplitPane splitPane = (JSplitPane) guiInstance.getLoggerPanel().getParent();
        boolean isVisible = guiInstance.getLoggerPanel().isVisible();
        toggleVisibility(guiInstance, splitPane, isVisible);
    }
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Toggles the visibility of the LoggerPanel.
 *
 * @param guiInstance the GuiPackage instance
 * @param splitPane the JSplitPane containing the LoggerPanel
 * @param isVisible the current visibility state of the LoggerPanel
 */
private static void toggleVisibility(GuiPackage guiInstance, JSplitPane splitPane, boolean isVisible) {
    if (!isVisible) {
        splitPane.setDividerSize(UIManager.getInt("SplitPane.dividerSize"));
        guiInstance.getLoggerPanel().setVisible(true);
        splitPane.setDividerLocation(0.8);
        guiInstance.getMenuItemLoggerPanel().getModel().setSelected(true);
    } else {
        guiInstance.getLoggerPanel().setVisible(false);
        splitPane.setDividerSize(0);
        guiInstance.getMenuItemLoggerPanel().getModel().setSelected(false);
    }
}

