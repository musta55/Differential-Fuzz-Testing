public MenuInfo(String label, JMeterGUIComponent item, String classFullName) {
    this.label = label;
    guiComp = item;
    className = classFullName;
    sortOrder = loadSortOrder(classFullName);
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

