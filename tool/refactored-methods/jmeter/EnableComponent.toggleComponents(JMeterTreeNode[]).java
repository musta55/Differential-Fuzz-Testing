private static void toggleComponents(JMeterTreeNode[] nodes) {
    GuiPackage pack = GuiPackage.getInstance();
    for (JMeterTreeNode node : nodes) {
        boolean enable = !node.isEnabled();
        setComponentEnabled(node, enable, pack);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static void setComponentEnabled(JMeterTreeNode node, boolean enable, GuiPackage pack) {
    node.setEnabled(enable);
    pack.getGui(node.getTestElement()).setEnabled(enable);
}

