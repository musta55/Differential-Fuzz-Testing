private void setNodePath() {
    List<String> nodePath = new ArrayList<>();
    if (selectedNode != null) {
        for (TreeNode node : selectedNode.getPath()) {
            nodePath.add(((JMeterTreeNode) node).getName());
        }
    }
    setProperty(new CollectionProperty(NODE_PATH, nodePath));
}