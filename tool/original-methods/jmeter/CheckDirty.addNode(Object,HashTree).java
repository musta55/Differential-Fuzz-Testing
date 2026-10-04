/**
 * The tree traverses itself depth-first, calling addNode for each
 * object it encounters as it goes.
 */
@Override
public void addNode(Object node, HashTree subTree) {
    if (log.isDebugEnabled()) {
        log.debug("Node is class: {}", node.getClass());
    }
    JMeterTreeNode treeNode = (JMeterTreeNode) node;
    if (checkMode) {
        // Only check if we have not found any differences so far
        if (!dirty) {
            if (previousGuiItems.containsKey(treeNode)) {
                if (!previousGuiItems.get(treeNode).equals(treeNode.getTestElement())) {
                    dirty = true;
                }
            } else {
                dirty = true;
            }
        }
    } else if (removeMode) {
        previousGuiItems.remove(treeNode);
    } else {
        previousGuiItems.put(treeNode, (TestElement) treeNode.getTestElement().clone());
    }
}