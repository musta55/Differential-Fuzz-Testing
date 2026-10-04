public static JMeterTreeNode[] cloneTreeNodes(JMeterTreeNode[] nodes) {
    JMeterTreeNode[] treeNodes = new JMeterTreeNode[nodes.length];
    for (int i = 0; i < nodes.length; i++) {
        treeNodes[i] = cloneTreeNode(nodes[i]);
    }
    return treeNodes;
}