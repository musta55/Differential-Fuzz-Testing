public void appendValue(DblArray key, int value) {
    int hash = key.hashCode();
    int ix = indexFor(hash, _data.length);
    if (_data[ix] == null) {
        createNewEntry(ix, key, value);
    } else if (_data[ix].add(key, value)) {
        _size++;
    }
    if (_size >= LOAD_FACTOR * _data.length) {
        resize();
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void createNewEntry(int index, DblArray key, int value) {
    _data[index] = new DArrayIListEntry(new DblArray(key), value);
    _size++;
}

