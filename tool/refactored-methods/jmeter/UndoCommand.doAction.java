@Override
public void doAction(ActionEvent e) throws IllegalUserActionException {
    GuiPackage guiPackage = GuiPackage.getInstance();
    final String command = e.getActionCommand();
    if (command.equals(ActionNames.UNDO)) {
        performUndo(guiPackage);
    } else if (command.equals(ActionNames.REDO)) {
        performRedo(guiPackage);
    } else {
        throw new IllegalArgumentException("Wrong action called: " + command);
    }
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Perform the undo operation.
 *
 * @param guiPackage the GuiPackage instance
 */
private static void performUndo(GuiPackage guiPackage) {
    guiPackage.undo();
}

/**
 * Perform the redo operation.
 *
 * @param guiPackage the GuiPackage instance
 */
private static void performRedo(GuiPackage guiPackage) {
    guiPackage.redo();
}

