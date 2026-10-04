boolean add(T child) {
    if (child.getParentId() != null && child.getParentId() < 1) {
        child.setParentId(null);
    }
    if ((item == null && child.getParentId() == null) || (item != null && item.getId().equals(child.getParentId()))) {
        children.put(child.getId(), new FileTree<>(child));
        return true;
    }
    for (Entry<Long, FileTree<T>> e : children.entrySet()) {
        if (e.getValue().add(child)) {
            return true;
        }
    }
    return false;
}