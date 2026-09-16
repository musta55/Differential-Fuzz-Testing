@Override
public JMenuItem[] getMenuItemsAtLocation(MENU_LOCATION location) {
    if (location == MENU_LOCATION.TOOLS) {
        JMenuItem menuItemIC = createMenuItem(EXPORT_NAMES);
        return new JMenuItem[] { menuItemIC };
    }
    return new JMenuItem[0];
}
// ---- helper method(s) introduced by the refactoring ----
private static String formatSampleNames(Set<String> sampleNames) {
    StringBuilder builder = new StringBuilder();
    for (String sampleName : sampleNames) {
        builder.append(sampleName).append('|');
    }
    builder.setLength(builder.length() - 1);
    return builder.toString();
}

private static JMenuItem createMenuItem(String actionName) {
    JMenuItem menuItemIC = new JMenuItem(JMeterUtils.getResString(actionName), KeyEvent.VK_UNDEFINED);
    menuItemIC.setName(actionName);
    menuItemIC.setActionCommand(actionName);
    menuItemIC.setAccelerator(null);
    menuItemIC.addActionListener(ActionRouter.getInstance());
    return menuItemIC;
}

