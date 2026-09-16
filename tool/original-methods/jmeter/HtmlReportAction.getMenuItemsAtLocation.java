@Override
public JMenuItem[] getMenuItemsAtLocation(MENU_LOCATION location) {
    if (location != MENU_LOCATION.TOOLS) {
        return new JMenuItem[0];
    }
    // Use the action name as resource key because the action name is used by JMeterMenuBar too when changing languages.
    JMenuItem menuItem = new JMenuItem(JMeterUtils.getResString(ActionNames.HTML_REPORT), KeyEvent.VK_UNDEFINED);
    menuItem.setName(ActionNames.HTML_REPORT);
    menuItem.setActionCommand(ActionNames.HTML_REPORT);
    menuItem.setAccelerator(null);
    menuItem.addActionListener(ActionRouter.getInstance());
    return new JMenuItem[] { menuItem };
}