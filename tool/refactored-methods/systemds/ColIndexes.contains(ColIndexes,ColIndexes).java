public boolean contains(ColIndexes first, ColIndexes second) {
    if (first == null || second == null)
        return false;
    return _indexes.contains(first._indexes.get(0)) || _indexes.contains(second._indexes.get(0));
}