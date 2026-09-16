@Override
public boolean equals(Object o) {
    if (this == o) {
        return true;
    }
    if (!isSameClass(o)) {
        return false;
    }
    EntityManagerEntry that = (EntityManagerEntry) o;
    return hasSameQualifier(that);
}
// ---- helper method(s) introduced by the refactoring ----
private boolean isSameClass(Object o) {
    return o != null && getClass() == o.getClass();
}

private boolean hasSameQualifier(EntityManagerEntry that) {
    return qualifier.equals(that.qualifier);
}

