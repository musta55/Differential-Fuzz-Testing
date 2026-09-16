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
            initTextNode((Text) node, mTreeNode);
            break;
        case Node.CDATA_SECTION_NODE:
            initCDATASectionNode((CDATASection) node, mTreeNode);
            break;
        case Node.COMMENT_NODE:
            initCommentNode((Comment) node, mTreeNode);
            break;
        default:
            // if other node type, we will just skip it
            break;
    }
}