/**
 * init element node
 *
 * @param node
 * @param mTreeNode
 * @throws SAXException
 */
private static void initElementNode(Node node, DefaultMutableTreeNode mTreeNode) throws SAXException {
    String nodeName = node.getNodeName();
    NodeList childNodes = node.getChildNodes();
    XMLDefaultMutableTreeNode childTreeNode = new XMLDefaultMutableTreeNode(nodeName, node);
    mTreeNode.add(childTreeNode);
    initAttributeNode(node, childTreeNode);
    for (int i = 0; i < childNodes.getLength(); i++) {
        Node childNode = childNodes.item(i);
        initNode(childNode, childTreeNode);
    }
}