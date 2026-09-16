@Override
public final Collection<T> toChoices(Collection<String> inIds) {
    Collection<T> c = new ArrayList<>();
    for (String id : inIds) {
        addIfValid(c, id);
    }
    return c;
}
// ---- helper method(s) introduced by the refactoring ----
private void addIfValid(Collection<T> c, String id) {
    if (ids.contains(id)) {
        T e = fromId(id);
        if (e != null) {
            c.add(e);
        }
    }
}

