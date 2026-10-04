public static JMeterTreeNode[] cloneTreeNodes(JMeterTreeNode[] nodes) {
    return java.util.Arrays.stream(nodes).map(Copy::cloneTreeNode).toArray(JMeterTreeNode[]::new);
}
// ---- helper method(s) introduced by the refactoring ----
private static void handleClipboardException(Exception ex, String errorMessageKey) {
    log.error("Clipboard error: {}", ex.getMessage(), ex);
    JOptionPane.showMessageDialog(GuiPackage.getInstance().getMainFrame(), JMeterUtils.getResString(errorMessageKey) + ":\n" + ex.getLocalizedMessage(), JMeterUtils.getResString("error_title"), JOptionPane.ERROR_MESSAGE);
}

