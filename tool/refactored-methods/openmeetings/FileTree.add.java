boolean add(T child) {
    if (child.getParentId() != null && child.getParentId() < 1) {
        child.setParentId(null);
    }
    if (canAddChild(child)) {
        children.put(child.getId(), new FileTree<>(child));
        return true;
    }
    return addChildToChildren(child);
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

