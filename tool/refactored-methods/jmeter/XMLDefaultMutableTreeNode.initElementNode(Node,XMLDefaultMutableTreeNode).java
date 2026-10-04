/**
 * init element node
 *
 * @param node
 * @param mTreeNode
 * @throws SAXException
 */
private static void initElementNode(Node node, XMLDefaultMutableTreeNode mTreeNode) throws SAXException {
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
// ---- helper method(s) introduced by the refactoring ----
/**
 * add comment Node
 *
 * @param node
 * @param mTreeNode
 * @throws SAXException
 */
private static void addCommentNode(Comment node, XMLDefaultMutableTreeNode mTreeNode) throws SAXException {
    String data = node.getData();
    if (data != null && !data.isEmpty()) {
        // $NON-NLS-1$ $NON-NLS-2$
        String value = "<!--" + data + "-->";
        XMLDefaultMutableTreeNode commentNode = new XMLDefaultMutableTreeNode(value, node);
        mTreeNode.add(commentNode);
    }
}

/**
 * add CDATASection Node
 *
 * @param node
 * @param mTreeNode
 * @throws SAXException
 */
private static void addCDATASectionNode(CDATASection node, XMLDefaultMutableTreeNode mTreeNode) throws SAXException {
    String data = node.getData();
    if (data != null && !data.isEmpty()) {
        // $NON-NLS-1$ $NON-NLS-2$
        String value = "<![CDATA[" + data + "]]>";
        XMLDefaultMutableTreeNode cdataNode = new XMLDefaultMutableTreeNode(value, node);
        mTreeNode.add(cdataNode);
    }
}

/**
 * add the TextNode
 *
 * @param node
 * @param mTreeNode
 * @throws SAXException
 */
private static void addTextNode(Text node, XMLDefaultMutableTreeNode mTreeNode) throws SAXException {
    String text = node.getNodeValue().trim();
    if (text != null && !text.isEmpty()) {
        XMLDefaultMutableTreeNode textNode = new XMLDefaultMutableTreeNode(text, node);
        mTreeNode.add(textNode);
    }
}

