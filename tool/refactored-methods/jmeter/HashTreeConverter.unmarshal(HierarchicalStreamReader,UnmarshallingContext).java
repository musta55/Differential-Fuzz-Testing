/**
 * {@inheritDoc}
 */
@Override
public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
    HashTree tree = (HashTree) createCollection(context.getRequiredType());
    while (reader.hasMoreChildren()) {
        reader.moveDown();
        Object key = readBareItem(reader, context, tree);
        reader.moveUp();
        reader.moveDown();
        HashTree value = (HashTree) readBareItem(reader, context, tree);
        reader.moveUp();
        tree.set(key, value);
    }
    return tree;
}