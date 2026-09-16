@Override
public void actionPerformed(ActionEvent e) {
    if (isRelevantAction(e.getActionCommand())) {
        doAction(e);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static boolean isRelevantAction(String actionCommand) {
    return actionCommand.equals(ActionNames.EXIT) || actionCommand.equals(ActionNames.UNDO) || actionCommand.equals(ActionNames.REDO);
}

private void handleSubTreeSaved(HashTree subTree) {
    previousGuiItems.clear();
    subTree.traverse(this);
}

private void handleSubTreeLoaded(ListedHashTree addTree) {
    addTree.traverse(this);
}

private void handleAddAll() {
    previousGuiItems.clear();
    GuiPackage.getInstance().getTreeModel().getTestPlan().traverse(this);
}

private void handleCheckRemoveOrCut() {
    GuiPackage guiPackage = GuiPackage.getInstance();
    JMeterTreeNode[] nodes = guiPackage.getTreeListener().getSelectedNodes();
    removeMode = true;
    try {
        for (int i = nodes.length - 1; i >= 0; i--) {
            guiPackage.getTreeModel().getCurrentSubTree(nodes[i]).traverse(this);
        }
    } finally {
        removeMode = false;
    }
}

private void handleSubTreeMerged() {
    dirty = true;
}

private void handleUndoOrRedo() {
    dirty = GuiPackage.getInstance().isDirty();
    log.debug("Restoring dirty after undo/redo");
    previousGuiItems.clear();
    GuiPackage.getInstance().getTreeModel().getTestPlan().traverse(this);
}

private void handleDefaultAction() {
    dirty = false;
    checkMode = true;
    try {
        HashTree wholeTree = GuiPackage.getInstance().getTreeModel().getTestPlan();
        wholeTree.traverse(this);
    } finally {
        checkMode = false;
    }
}

