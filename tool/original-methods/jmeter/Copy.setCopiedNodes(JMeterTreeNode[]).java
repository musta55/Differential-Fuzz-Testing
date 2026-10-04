public static void setCopiedNodes(JMeterTreeNode[] nodes) {
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    try {
        JMeterTreeNodeTransferable transferable = new JMeterTreeNodeTransferable();
        transferable.setTransferData(nodes);
        clipboard.setContents(transferable, null);
    } catch (Exception ex) {
        log.error("Clipboard node read error: {}", ex.getMessage(), ex);
        JOptionPane.showMessageDialog(GuiPackage.getInstance().getMainFrame(), //$NON-NLS-1$ //$NON-NLS-2$
        JMeterUtils.getResString("clipboard_node_read_error") + ":\n" + ex.getLocalizedMessage(), JMeterUtils.getResString("error_title"), //$NON-NLS-1$
        JOptionPane.ERROR_MESSAGE);
    }
}