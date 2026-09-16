/**
 * Returns whether the menu item represented by this MenuInfo object should be enabled
 * @param actionCommand    the action command name for the menu item
 * @return true when menu item should be enabled, false otherwise.
 */
public boolean getEnabled(String actionCommand) {
    if (ActionNames.ADD.equals(actionCommand)) {
        return guiComp.canBeAdded();
    } else {
        return true;
    }
}