private static void enableComponents(JMeterTreeNode[] nodes, boolean enable) {
    GuiPackage pack = GuiPackage.getInstance();
    for (JMeterTreeNode node : nodes) {
        node.setEnabled(enable);
        pack.getGui(node.getTestElement()).setEnabled(enable);
    }
}