@Override
public Set<Long> apply(Set<Long> set0, Set<Long> set1) {
    if (set0.isEmpty()) {
        return set1;
    }
    if (set1.isEmpty()) {
        return set0;
    }
    // Merging left-right is identical to merging right-left
    set0.addAll(set1);
    return set0;
}