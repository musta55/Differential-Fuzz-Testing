private static void toggleComponents(JMeterTreeNode[] nodes) {
    GuiPackage pack = GuiPackage.getInstance();
    for (JMeterTreeNode node : nodes) {
        boolean enable = !node.isEnabled();
        node.setEnabled(enable);
        pack.getGui(node.getTestElement()).setEnabled(enable);
    }
}