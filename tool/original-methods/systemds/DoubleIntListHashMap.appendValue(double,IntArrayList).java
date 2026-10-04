private void appendValue(double key, IntArrayList value) {
    // compute entry index position
    int hash = hash(key);
    int ix = indexFor(hash, _data.length);
    // add new table entry (constant time)
    DIListEntry enew = new DIListEntry(key, value);
    // colliding entries / null
    enew.next = _data[ix];
    _data[ix] = enew;
    if (enew.next != null && enew.next.key == key) {
        enew.next = enew.next.next;
        _size--;
    }
    _size++;
    // resize if necessary
    if (_size >= LOAD_FACTOR * _data.length)
        resize();
}