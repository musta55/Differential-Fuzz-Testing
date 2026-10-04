public static JMeterTreeNode[] getCopiedNodes() {
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    if (clipboard.isDataFlavorAvailable(JMeterTreeNodeTransferable.JMETER_TREE_NODE_ARRAY_DATA_FLAVOR)) {
        try {
            return (JMeterTreeNode[]) clipboard.getData(JMeterTreeNodeTransferable.JMETER_TREE_NODE_ARRAY_DATA_FLAVOR);
        } catch (Exception ex) {
            handleClipboardException(ex, "clipboard_node_read_error");
        }
    }
    return null;
}
// ---- helper method(s) introduced by the refactoring ----
private static void handleClipboardException(Exception ex, String errorMessageKey) {
    log.error("Clipboard error: {}", ex.getMessage(), ex);
    JOptionPane.showMessageDialog(GuiPackage.getInstance().getMainFrame(), JMeterUtils.getResString(errorMessageKey) + ":\n" + ex.getLocalizedMessage(), JMeterUtils.getResString("error_title"), JOptionPane.ERROR_MESSAGE);
}

