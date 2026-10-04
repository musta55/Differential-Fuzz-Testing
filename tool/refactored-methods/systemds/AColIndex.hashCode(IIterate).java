private static int hashCode(IIterate it) {
    int res = 1;
    while (it.hasNext()) {
        res = 31 * res + it.next();
    }
    return res;
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

