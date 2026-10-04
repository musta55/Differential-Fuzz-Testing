private void setNodePath() {
    List<String> nodePath = new ArrayList<>();
    if (selectedNode != null) {
        TreeNode[] path = selectedNode.getPath();
        for (TreeNode node : path) {
            nodePath.add(((JMeterTreeNode) node).getName());
        }
    }
    setProperty(new CollectionProperty(NODE_PATH, nodePath));
}