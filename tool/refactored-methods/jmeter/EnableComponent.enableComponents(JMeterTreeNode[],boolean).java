private static void enableComponents(JMeterTreeNode[] nodes, boolean enable) {
    GuiPackage pack = GuiPackage.getInstance();
    for (JMeterTreeNode node : nodes) {
        setComponentEnabled(node, enable, pack);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static void setComponentEnabled(JMeterTreeNode node, boolean enable, GuiPackage pack) {
    node.setEnabled(enable);
    pack.getGui(node.getTestElement()).setEnabled(enable);
}

