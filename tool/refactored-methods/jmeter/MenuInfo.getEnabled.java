/**
 * Returns whether the menu item represented by this MenuInfo object should be enabled
 * @param actionCommand    the action command name for the menu item
 * @return true when menu item should be enabled, false otherwise.
 */
public boolean getEnabled(String actionCommand) {
    if (ActionNames.ADD.equals(actionCommand)) {
        return guiComp.canBeAdded();
    }
    return true;
}
// ---- helper method(s) introduced by the refactoring ----
private static int loadSortOrder(String classFullName) {
    try {
        GUIMenuSortOrder menuSortOrder = Class.forName(classFullName, false, MenuInfo.class.getClassLoader()).getDeclaredAnnotation(GUIMenuSortOrder.class);
        if (menuSortOrder != null) {
            return menuSortOrder.value();
        }
    } catch (ClassNotFoundException ignored) {
        // NOOP
    }
    return SORT_ORDER_DEFAULT;
}

