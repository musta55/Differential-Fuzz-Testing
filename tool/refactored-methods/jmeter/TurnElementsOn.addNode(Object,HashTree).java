/**
 * {@inheritDoc}
 */
@Override
public void addNode(Object node, HashTree subTree) {
    enableTestElement(node);
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Enables the test element if it is an instance of TestElement and not an instance of TestPlan.
 *
 * @param node the node to potentially enable
 */
private static void enableTestElement(Object node) {
    if (node instanceof TestElement && !(node instanceof TestPlan)) {
        ((TestElement) node).setRunningVersion(true);
    }
}

