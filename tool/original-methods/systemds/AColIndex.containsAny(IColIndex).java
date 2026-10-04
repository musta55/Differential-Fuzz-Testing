@Override
public boolean containsAny(IColIndex idx) {
    if (idx instanceof TwoRangesIndex) {
        TwoRangesIndex o = (TwoRangesIndex) idx;
        return this.containsAny(o.idx1) || this.containsAny(o.idx2);
    } else if (idx instanceof CombinedIndex) {
        CombinedIndex ci = (CombinedIndex) idx;
        return containsAny(ci.l) || containsAny(ci.r);
    } else {
        final IIterate it = idx.iterator();
        while (it.hasNext()) if (contains(it.next()))
            return true;
        return false;
    }
}