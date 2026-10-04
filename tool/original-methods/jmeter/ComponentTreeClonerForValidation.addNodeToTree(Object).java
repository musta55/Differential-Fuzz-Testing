/**
 * @see org.apache.jmeter.engine.TreeCloner#addNodeToTree(java.lang.Object)
 */
@Override
protected Object addNodeToTree(Object node) {
    Object clonedNode = super.addNodeToTree(node);
    if (VALIDATION_TPC_FORCE_100_PERCENT && clonedNode instanceof ThroughputController) {
        ThroughputController tc = (ThroughputController) clonedNode;
        if (tc.getStyle() == ThroughputController.BYPERCENT) {
            tc.setPercentThroughput(100);
        }
    }
    return clonedNode;
}