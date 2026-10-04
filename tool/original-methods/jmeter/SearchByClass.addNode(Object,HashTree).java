/**
 * {@inheritDoc}
 */
@SuppressWarnings("unchecked")
@Override
public void addNode(Object node, HashTree subTree) {
    if (searchClass.isAssignableFrom(node.getClass())) {
        objectsOfClass.add((T) node);
        ListedHashTree tree = new ListedHashTree(node);
        tree.set(node, subTree);
        subTrees.put(node, tree);
    }
}