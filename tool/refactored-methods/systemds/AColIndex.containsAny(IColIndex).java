@Override
public boolean containsAny(IColIndex idx) {
    if (idx instanceof TwoRangesIndex) {
        TwoRangesIndex twoRangesIndex = (TwoRangesIndex) idx;
        return containsAny(twoRangesIndex.idx1) || containsAny(twoRangesIndex.idx2);
    } else if (idx instanceof CombinedIndex) {
        CombinedIndex combinedIndex = (CombinedIndex) idx;
        return containsAny(combinedIndex.l) || containsAny(combinedIndex.r);
    } else {
        IIterate iterator = idx.iterator();
        while (iterator.hasNext()) {
            if (contains(iterator.next())) {
                return true;
            }
        }
        return false;
    }
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

