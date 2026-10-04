/**
 * Inserts (or merges) the tree into the GUI.
 * Does not check if the previous tree has been saved.
 * Clears the existing GUI test plan if we are inserting a complete plan.
 * @param id the id for the ActionEvent that is created
 * @param tree the tree to load
 * @param merging true if the tree is to be merged; false if it is to replace the existing tree
 * @return true if the loaded tree was a full test plan
 * @throws IllegalUserActionException if the tree cannot be merged at the selected position or the tree is empty
 */
// Does not appear to be used externally; called by #loadProjectFile()
public static boolean insertLoadedTree(final int id, final HashTree tree, final boolean merging) throws IllegalUserActionException {
    if (tree == null) {
        throw new IllegalUserActionException("Empty TestPlan or error reading test plan - see log file");
    }
    final boolean isTestPlan = tree.getArray()[0] instanceof TestPlan;
    // If we are loading a new test plan, initialize the tree with the testplan node we are loading
    final GuiPackage guiInstance = GuiPackage.getInstance();
    if (isTestPlan && !merging) {
        // Why does this not call guiInstance.clearTestPlan() ?
        // Is there a reason for not clearing everything?
        guiInstance.clearTestPlan((TestElement) tree.getArray()[0]);
    }
    if (merging) {
        // Check if target of merge is reasonable
        final TestElement te = (TestElement) tree.getArray()[0];
        if (!(te instanceof TestPlan)) {
            // These are handled specially by addToTree
            final boolean ok = MenuFactory.canAddTo(guiInstance.getCurrentNode(), te);
            if (!ok) {
                String name = te.getName();
                String className = te.getClass().getName();
                className = className.substring(className.lastIndexOf('.') + 1);
                throw new IllegalUserActionException("Can't merge " + name + " (" + className + ") here");
            }
        }
    }
    final HashTree newTree = guiInstance.addSubTree(tree);
    guiInstance.updateCurrentGui();
    guiInstance.getMainFrame().getTree().setSelectionPath(new TreePath(((JMeterTreeNode) newTree.getArray()[0]).getPath()));
    final HashTree subTree = guiInstance.getCurrentSubTree();
    // Send different event whether we are merging a test plan into another test plan,
    // or loading a testplan from scratch
    ActionEvent actionEvent = new ActionEvent(subTree.get(subTree.getArray()[subTree.size() - 1]), id, merging ? ActionNames.SUB_TREE_MERGED : ActionNames.SUB_TREE_LOADED);
    ActionRouter.getInstance().actionPerformed(actionEvent);
    final JTree jTree = guiInstance.getMainFrame().getTree();
    if (EXPAND_TREE && !merging) {
        // don't automatically expand when merging
        for (int i = 0; i < jTree.getRowCount(); i++) {
            jTree.expandRow(i);
        }
    } else {
        jTree.expandRow(0);
    }
    jTree.setSelectionPath(jTree.getPathForRow(1));
    FocusRequester.requestFocus(jTree);
    return isTestPlan;
}