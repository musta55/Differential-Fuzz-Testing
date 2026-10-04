@Override
public Set<Long> apply(Set<Long> set0, Set<Long> set1) {
    if (isSetEmpty(set0)) {
        return set1;
    }
    if (isSetEmpty(set1)) {
        return set0;
    }
    // Merging left-right is identical to merging right-left
    mergeSets(set0, set1);
    return set0;
}
// ---- helper method(s) introduced by the refactoring ----
private boolean isSetEmpty(Set<Long> set) {
    return set.isEmpty();
}

private void mergeSets(Set<Long> targetSet, Set<Long> sourceSet) {
    targetSet.addAll(sourceSet);
}

