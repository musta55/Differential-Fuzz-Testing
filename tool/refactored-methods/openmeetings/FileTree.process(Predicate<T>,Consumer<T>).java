void process(Predicate<T> invalid, Consumer<T> consumer) {
    if (item != null) {
        if (invalid.test(item)) {
            // we will not process invalid and i's children
            return;
        }
        consumer.accept(item);
    }
    processChildren(invalid, consumer);
}
// ---- helper method(s) introduced by the refactoring ----
private boolean canAddChild(T child) {
    return (item == null && child.getParentId() == null) || (item != null && item.getId().equals(child.getParentId()));
}

private boolean addChildToChildren(T child) {
    for (Entry<Long, FileTree<T>> e : children.entrySet()) {
        if (e.getValue().add(child)) {
            return true;
        }
    }
    return false;
}

private void processChildren(Predicate<T> invalid, Consumer<T> consumer) {
    if (!children.isEmpty()) {
        children.forEach((id, e) -> e.process(invalid, consumer));
    }
}

