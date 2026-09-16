private static JMenuItem createMenuItem(String name, String actionCommand) {
    JMenuItem menuItem = new JMenuItem(JMeterUtils.getResString(name));
    menuItem.setName(name);
    menuItem.addActionListener(ActionRouter.getInstance());
    menuItem.setActionCommand(actionCommand);
    return menuItem;
}
// ---- helper method(s) introduced by the refactoring ----
private static JRadioButton createRadioButton(ButtonGroup group, String key) {
    JRadioButton radioButton = new JRadioButton(JMeterUtils.getResString(key));
    group.add(radioButton);
    return radioButton;
}

