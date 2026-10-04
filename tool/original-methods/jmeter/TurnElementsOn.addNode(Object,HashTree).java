/**
 * {@inheritDoc}
 */
@Override
public void addNode(Object node, HashTree subTree) {
    if (node instanceof TestElement && !(node instanceof TestPlan)) {
        ((TestElement) node).setRunningVersion(true);
    }
}