/**
 * init node
 *
 * @param node
 * @param mTreeNode
 * @throws SAXException
 */
private static void initNode(Node node, XMLDefaultMutableTreeNode mTreeNode) throws SAXException {
    switch(node.getNodeType()) {
        case Node.ELEMENT_NODE:
            initElementNode(node, mTreeNode);
            break;
        case Node.TEXT_NODE:
            addTextNode((Text) node, mTreeNode);
            break;
        case Node.CDATA_SECTION_NODE:
            addCDATASectionNode((CDATASection) node, mTreeNode);
            break;
        case Node.COMMENT_NODE:
            addCommentNode((Comment) node, mTreeNode);
            break;
        default:
            // if other node type, we will just skip it
            break;
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

