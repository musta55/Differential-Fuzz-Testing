@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("FileTree[type ='");
    sb.append(item == null ? "root" : item.getType());
    sb.append("'");
    if (item != null) {
        sb.append(", name='").append(item.getName()).append("'");
    }
    if (!children.isEmpty()) {
        sb.append(", children='").append(children).append("'");
    }
    sb.append("]");
    return sb.toString();
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

