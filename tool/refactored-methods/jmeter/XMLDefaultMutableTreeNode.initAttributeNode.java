/**
 * init attribute node
 *
 * @param node
 * @param mTreeNode
 * @throws SAXException
 */
private static void initAttributeNode(Node node, XMLDefaultMutableTreeNode mTreeNode) throws SAXException {
    NamedNodeMap nm = node.getAttributes();
    for (int i = 0; i < nm.getLength(); i++) {
        Attr nmNode = (Attr) nm.item(i);
        // $NON-NLS-1$ $NON-NLS-2$
        String value = nmNode.getName() + " = \"" + nmNode.getValue() + "\"";
        XMLDefaultMutableTreeNode attributeNode = new XMLDefaultMutableTreeNode(value, nmNode);
        mTreeNode.add(attributeNode);
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

