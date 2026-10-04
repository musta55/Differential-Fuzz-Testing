@Override
public void doAction(ActionEvent e) throws IllegalUserActionException {
    GuiPackage guiPackage = GuiPackage.getInstance();
    final String command = e.getActionCommand();
    if (command.equals(ActionNames.UNDO)) {
        guiPackage.undo();
    } else if (command.equals(ActionNames.REDO)) {
        guiPackage.redo();
    } else {
        throw new IllegalArgumentException("Wrong action called: " + command);
    }
}