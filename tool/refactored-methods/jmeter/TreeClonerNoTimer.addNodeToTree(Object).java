/**
 * Doesn't add Timer to tree
 * @see org.apache.jmeter.engine.TreeCloner#addNodeToTree(java.lang.Object)
 */
@Override
protected Object addNodeToTree(Object node) {
    if (node instanceof Timer) {
        log.debug("Ignoring timer node: {}", node);
        // don't add the timer
        return node;
    }
    return super.addNodeToTree(node);
}