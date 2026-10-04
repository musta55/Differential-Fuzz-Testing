/**
 * @see Command#doAction(ActionEvent)
 */
@Override
public void doAction(ActionEvent e) {
    String action = e.getActionCommand();
    if (action.equals(ActionNames.SUB_TREE_SAVED)) {
        handleSubTreeSaved((HashTree) e.getSource());
    } else if (action.equals(ActionNames.SUB_TREE_LOADED)) {
        handleSubTreeLoaded((ListedHashTree) e.getSource());
    } else if (action.equals(ActionNames.ADD_ALL)) {
        handleAddAll();
    } else if (action.equals(ActionNames.CHECK_REMOVE) || action.equals(ActionNames.CHECK_CUT)) {
        handleCheckRemoveOrCut();
    } else if (action.equals(ActionNames.SUB_TREE_MERGED)) {
        handleSubTreeMerged();
    } else if (action.equals(ActionNames.UNDO) || action.equals(ActionNames.REDO)) {
        handleUndoOrRedo();
    } else {
        handleDefaultAction();
    }
    GuiPackage.getInstance().setDirty(dirty);
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

