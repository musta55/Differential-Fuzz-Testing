/**
 * init attribute node
 *
 * @param node
 * @param mTreeNode
 * @throws SAXException
 */
private static void initAttributeNode(Node node, DefaultMutableTreeNode mTreeNode) throws SAXException {
    NamedNodeMap nm = node.getAttributes();
    for (int i = 0; i < nm.getLength(); i++) {
        Attr nmNode = (Attr) nm.item(i);
        // $NON-NLS-1$ $NON-NLS-2$
        String value = nmNode.getName() + " = \"" + nmNode.getValue() + "\"";
        XMLDefaultMutableTreeNode attributeNode = new XMLDefaultMutableTreeNode(value, nmNode);
        mTreeNode.add(attributeNode);
    }
}