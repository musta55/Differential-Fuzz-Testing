@Override
public long size() {
    return getFilteredSize();
}
// ---- helper method(s) introduced by the refactoring ----
private Iterator<? extends T> getFilteredIterator(long first, long count) {
    return hasGroupAdminLevel(getRights()) ? getDao().adminGet(search, getUserId(), first, count, getSort()).iterator() : getDao().adminGet(search, first, count, getSort()).iterator();
}

private long getFilteredSize() {
    return hasGroupAdminLevel(getRights()) ? getDao().adminCount(search, getUserId()) : getDao().adminCount(search);
}

