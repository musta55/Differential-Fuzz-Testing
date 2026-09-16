private static JMenuItem createMenuItem(String name, String actionCommand) {
    JMenuItem addThinkTimesToChildren = new JMenuItem(JMeterUtils.getResString(name));
    addThinkTimesToChildren.setName(name);
    addThinkTimesToChildren.addActionListener(ActionRouter.getInstance());
    addThinkTimesToChildren.setActionCommand(actionCommand);
    return addThinkTimesToChildren;
}