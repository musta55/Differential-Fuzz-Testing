public boolean contains(ColIndexes a, ColIndexes b) {
    if (a == null || b == null)
        return false;
    return //
    _indexes.contains(a._indexes.get(0)) || _indexes.contains(b._indexes.get(0));
}