/**
 * {@inheritDoc}
 */
@SuppressWarnings("unchecked")
@Override
public void addNode(Object node, HashTree subTree) {
    if (searchClass.isAssignableFrom(node.getClass())) {
        objectsOfClass.add((T) node);
        setupListedHashTree(node, subTree);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void setupListedHashTree(Object node, HashTree subTree) {
    ListedHashTree tree = new ListedHashTree(node);
    tree.set(node, subTree);
    subTrees.put(node, tree);
}

