@Override
public void doAction(ActionEvent e) {
    int isConfirm = shouldShowConfirmationDialog() ? JOptionPane.YES_OPTION : showConfirmationDialog();
    if (isConfirm == JOptionPane.YES_OPTION) {
        // TODO - removes the nodes from the CheckDirty map - should it be done later, in case some can't be removed?
        ActionRouter.getInstance().actionPerformed(new ActionEvent(e.getSource(), e.getID(), ActionNames.CHECK_REMOVE));
        GuiPackage guiPackage = GuiPackage.getInstance();
        JMeterTreeNode[] nodes = guiPackage.getTreeListener().getSelectedNodes();
        // Save parent node for later
        TreePath // Save parent node for later
        newTreePath = guiPackage.getTreeListener().removedSelectedNode();
        for (int i = nodes.length - 1; i >= 0; i--) {
            removeNode(nodes[i]);
        }
        guiPackage.getTreeListener().getJTree().setSelectionPath(newTreePath);
        guiPackage.updateCurrentGui();
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static boolean shouldShowConfirmationDialog() {
    return !SKIP_CONFIRM;
}

private static int showConfirmationDialog() {
    return JOptionPane.showConfirmDialog(GuiPackage.getInstance().getMainFrame(), // $NON-NLS-1$
    JMeterUtils.getResString("remove_confirm_msg"), // $NON-NLS-1$
    JMeterUtils.getResString("remove_confirm_title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
}

