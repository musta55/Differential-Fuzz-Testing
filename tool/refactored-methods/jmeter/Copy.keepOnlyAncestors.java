/**
 * If a child and one of its ancestors are selected : only keep the ancestor
 * @param currentNodes JMeterTreeNode[]
 * @return JMeterTreeNode[]
 */
static JMeterTreeNode[] keepOnlyAncestors(JMeterTreeNode[] currentNodes) {
    return java.util.Arrays.stream(currentNodes).filter(node -> java.util.Arrays.stream(currentNodes).noneMatch(otherNode -> !node.equals(otherNode) && node.isNodeAncestor(otherNode))).toArray(JMeterTreeNode[]::new);
}
// ---- helper method(s) introduced by the refactoring ----
private static void handleClipboardException(Exception ex, String errorMessageKey) {
    log.error("Clipboard error: {}", ex.getMessage(), ex);
    JOptionPane.showMessageDialog(GuiPackage.getInstance().getMainFrame(), JMeterUtils.getResString(errorMessageKey) + ":\n" + ex.getLocalizedMessage(), JMeterUtils.getResString("error_title"), JOptionPane.ERROR_MESSAGE);
}

