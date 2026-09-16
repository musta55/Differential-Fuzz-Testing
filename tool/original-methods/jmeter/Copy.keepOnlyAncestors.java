/**
 * If a child and one of its ancestors are selected : only keep the ancestor
 * @param currentNodes JMeterTreeNode[]
 * @return JMeterTreeNode[]
 */
static JMeterTreeNode[] keepOnlyAncestors(JMeterTreeNode[] currentNodes) {
    List<JMeterTreeNode> nodes = new ArrayList<>();
    for (int i = 0; i < currentNodes.length; i++) {
        boolean exclude = false;
        for (int j = 0; j < currentNodes.length; j++) {
            if (i != j && currentNodes[i].isNodeAncestor(currentNodes[j])) {
                exclude = true;
                break;
            }
        }
        if (!exclude) {
            nodes.add(currentNodes[i]);
        }
    }
    return nodes.toArray(new JMeterTreeNode[nodes.size()]);
}