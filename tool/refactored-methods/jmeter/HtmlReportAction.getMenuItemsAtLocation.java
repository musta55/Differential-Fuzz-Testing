@Override
public JMenuItem[] getMenuItemsAtLocation(MENU_LOCATION location) {
    if (location != MENU_LOCATION.TOOLS) {
        return new JMenuItem[0];
    }
    return new JMenuItem[] { createHtmlReportMenuItem() };
}
// ---- helper method(s) introduced by the refactoring ----
private static JMenuItem createHtmlReportMenuItem() {
    JMenuItem menuItem = new JMenuItem(JMeterUtils.getResString(ActionNames.HTML_REPORT), KeyEvent.VK_UNDEFINED);
    menuItem.setName(ActionNames.HTML_REPORT);
    menuItem.setActionCommand(ActionNames.HTML_REPORT);
    menuItem.setAccelerator(null);
    menuItem.addActionListener(ActionRouter.getInstance());
    return menuItem;
}

