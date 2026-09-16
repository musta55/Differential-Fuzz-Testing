public static JMeterTreeNode[] getCopiedNodes() {
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    if (clipboard.isDataFlavorAvailable(JMeterTreeNodeTransferable.JMETER_TREE_NODE_ARRAY_DATA_FLAVOR)) {
        try {
            return (JMeterTreeNode[]) clipboard.getData(JMeterTreeNodeTransferable.JMETER_TREE_NODE_ARRAY_DATA_FLAVOR);
        } catch (Exception ex) {
            log.error("Clipboard node read error: {}", ex.getMessage(), ex);
            JOptionPane.showMessageDialog(GuiPackage.getInstance().getMainFrame(), //$NON-NLS-1$  //$NON-NLS-2$
            JMeterUtils.getResString("clipboard_node_read_error") + ":\n" + ex.getLocalizedMessage(), JMeterUtils.getResString("error_title"), //$NON-NLS-1$
            JOptionPane.ERROR_MESSAGE);
        }
    }
    return null;
}