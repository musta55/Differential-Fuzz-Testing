@SuppressWarnings("JdkObsolete")
private static void createSubTree(HashTree tree, JMeterTreeNode node) {
    Enumeration<?> e = node.children();
    while (e.hasMoreElements()) {
        JMeterTreeNode subNode = (JMeterTreeNode) e.nextElement();
        tree.add(subNode);
        createSubTree(tree.getTree(subNode), subNode);
    }
}