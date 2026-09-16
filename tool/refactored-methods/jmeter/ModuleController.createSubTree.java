private static void createSubTree(HashTree tree, JMeterTreeNode node) {
    for (Enumeration<?> e = node.children(); e.hasMoreElements(); ) {
        JMeterTreeNode subNode = (JMeterTreeNode) e.nextElement();
        tree.add(subNode);
        createSubTree(tree.getTree(subNode), subNode);
    }
}