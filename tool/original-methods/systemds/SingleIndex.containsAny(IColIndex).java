@Override
public boolean containsAny(IColIndex idx) {
    if (idx instanceof SingleIndex)
        return this.idx == idx.get(0);
    else
        // turn around the logic.
        return idx.contains(this.idx);
}