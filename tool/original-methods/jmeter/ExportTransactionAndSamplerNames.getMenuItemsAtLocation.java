@Override
public JMenuItem[] getMenuItemsAtLocation(MENU_LOCATION location) {
    if (location == MENU_LOCATION.TOOLS) {
        // Use the action name as resource key because the action name is used by JMeterMenuBar too when changing languages.
        JMenuItem menuItemIC = new JMenuItem(JMeterUtils.getResString(ExportTransactionAndSamplerNames.EXPORT_NAMES), KeyEvent.VK_UNDEFINED);
        menuItemIC.setName(ExportTransactionAndSamplerNames.EXPORT_NAMES);
        menuItemIC.setActionCommand(ExportTransactionAndSamplerNames.EXPORT_NAMES);
        menuItemIC.setAccelerator(null);
        menuItemIC.addActionListener(ActionRouter.getInstance());
        return new JMenuItem[] { menuItemIC };
    }
    return new JMenuItem[0];
}