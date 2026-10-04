private void appendValue(double key, IntArrayList value) {
    // compute entry index position
    int hash = hash(key);
    int ix = indexFor(hash, _data.length);
    // add new table entry (constant time)
    DIListEntry enew = new DIListEntry(key, value);
    // colliding entries / null
    enew.next = _data[ix];
    _data[ix] = enew;
    handleCollisionAndResize(enew, ix);
}
// ---- helper method(s) introduced by the refactoring ----
private void handleCollisionAndResize(DIListEntry enew, int ix) {
    if (enew.next != null && Util.eq(enew.next.key, enew.key)) {
        enew.next = enew.next.next;
        _size--;
    }
    _size++;
    // resize if necessary
    if (_size >= LOAD_FACTOR * _data.length)
        resize();
}

private void addNewEntry(int ix, double key, int value) {
    IntArrayList lstPtr = new IntArrayList();
    lstPtr.appendValue(value);
    _data[ix] = new DIListEntry(key, lstPtr);
    _size++;
    if (_size >= LOAD_FACTOR * _data.length)
        resize();
}

private void updateExistingEntry(int ix, double key, int value) {
    for (DIListEntry e = _data[ix]; e != null; e = e.next) {
        if (Util.eq(e.key, key)) {
            e.value.appendValue(value);
            break;
        } else if (e.next == null) {
            addNewEntryAtHead(ix, key, value, e);
            break;
        }
    }
}

private void addNewEntryAtHead(int ix, double key, int value, DIListEntry e) {
    IntArrayList lstPtr = new IntArrayList();
    lstPtr.appendValue(value);
    // Swap to place the new value, in front.
    DIListEntry eOld = _data[ix];
    _data[ix] = new DIListEntry(key, lstPtr);
    _data[ix].next = eOld;
    _size++;
    if (_size >= LOAD_FACTOR * _data.length)
        resize();
}

