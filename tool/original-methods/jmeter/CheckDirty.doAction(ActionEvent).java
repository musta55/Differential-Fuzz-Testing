/**
 * @see Command#doAction(ActionEvent)
 */
@Override
public void doAction(ActionEvent e) {
    String action = e.getActionCommand();
    if (action.equals(ActionNames.SUB_TREE_SAVED)) {
        previousGuiItems.clear();
        HashTree subTree = (HashTree) e.getSource();
        subTree.traverse(this);
    } else if (action.equals(ActionNames.SUB_TREE_LOADED)) {
        ListedHashTree addTree = (ListedHashTree) e.getSource();
        addTree.traverse(this);
    } else if (action.equals(ActionNames.ADD_ALL)) {
        previousGuiItems.clear();
        GuiPackage.getInstance().getTreeModel().getTestPlan().traverse(this);
    } else if (action.equals(ActionNames.CHECK_REMOVE) || action.equals(ActionNames.CHECK_CUT)) {
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
    // If we are merging in another test plan, we know the test plan is dirty now
    if (action.equals(ActionNames.SUB_TREE_MERGED)) {
        dirty = true;
    } else if (action.equals(ActionNames.UNDO) || action.equals(ActionNames.REDO)) {
        dirty = GuiPackage.getInstance().isDirty();
        log.debug("Restoring dirty after undo/redo");
        //remember
        previousGuiItems.clear();
        GuiPackage.getInstance().getTreeModel().getTestPlan().traverse(this);
    } else {
        dirty = false;
        checkMode = true;
        try {
            HashTree wholeTree = GuiPackage.getInstance().getTreeModel().getTestPlan();
            wholeTree.traverse(this);
        } finally {
            checkMode = false;
        }
    }
    GuiPackage.getInstance().setDirty(dirty);
}