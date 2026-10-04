public void appendValue(DblArray key, int value) {
    int hash = key.hashCode();
    int ix = indexFor(hash, _data.length);
    if (_data[ix] == null) {
        _data[ix] = new DArrayIListEntry(new DblArray(key), value);
        _size++;
    } else if (_data[ix].add(key, value))
        _size++;
    if (_size >= LOAD_FACTOR * _data.length)
        resize();
}