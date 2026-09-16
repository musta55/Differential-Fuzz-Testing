private JComponent getTreePanel() {
    //$NON-NLS-1$
    root = new DefaultMutableTreeNode("Root");
    treeModel = new DefaultTreeModel(root);
    resultsTree = new JTree(treeModel);
    resultsTree.setCellRenderer(new TreeNodeRenderer());
    resultsTree.setCellRenderer(new TreeNodeRenderer());
    resultsTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    resultsTree.addTreeSelectionListener(new Selector());
    resultsTree.setRootVisible(false);
    resultsTree.setShowsRootHandles(true);
    JScrollPane treePane = new JScrollPane(resultsTree);
    treePane.setPreferredSize(new Dimension(150, 50));
    JPanel panel = new JPanel(new GridLayout(1, 1));
    panel.add(treePane);
    return panel;
}