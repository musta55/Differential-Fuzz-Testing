/**
 * {@inheritDoc}
 */
@Override
public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
    boolean isKey = true;
    Object current = null;
    HashTree tree = (HashTree) createCollection(context.getRequiredType());
    while (reader.hasMoreChildren()) {
        reader.moveDown();
        Object item = readBareItem(reader, context, tree);
        if (isKey) {
            tree.add(item);
            current = item;
            isKey = false;
        } else {
            tree.set(current, (HashTree) item);
            isKey = true;
        }
        reader.moveUp();
    }
    return tree;
}