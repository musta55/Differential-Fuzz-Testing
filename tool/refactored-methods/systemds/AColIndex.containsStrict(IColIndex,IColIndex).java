@Override
public boolean containsStrict(IColIndex a, IColIndex b) {
    if (a == null || b == null || a.size() + b.size() != size()) {
        return false;
    }
    if (!allIndicesPresent(a)) {
        return false;
    }
    return allIndicesPresent(b);
}
// ---- helper method(s) introduced by the refactoring ----
private boolean allIndicesPresent(IColIndex index) {
    IIterate iterator = index.iterator();
    while (iterator.hasNext()) {
        if (findIndex(iterator.next()) < 0) {
            return false;
        }
    }
    return true;
}

