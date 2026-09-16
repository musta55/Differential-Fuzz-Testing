public static void setCopiedNodes(JMeterTreeNode[] nodes) {
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    try {
        JMeterTreeNodeTransferable transferable = new JMeterTreeNodeTransferable();
        transferable.setTransferData(nodes);
        clipboard.setContents(transferable, null);
    } catch (Exception ex) {
        handleClipboardException(ex, "clipboard_node_write_error");
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static void handleClipboardException(Exception ex, String errorMessageKey) {
    log.error("Clipboard error: {}", ex.getMessage(), ex);
    JOptionPane.showMessageDialog(GuiPackage.getInstance().getMainFrame(), JMeterUtils.getResString(errorMessageKey) + ":\n" + ex.getLocalizedMessage(), JMeterUtils.getResString("error_title"), JOptionPane.ERROR_MESSAGE);
}

