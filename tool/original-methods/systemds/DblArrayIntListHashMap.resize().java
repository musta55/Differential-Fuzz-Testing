private void resize() {
    // resize data array and copy existing contents
    DArrayIListEntry[] olddata = _data;
    _data = new DArrayIListEntry[_data.length * RESIZE_FACTOR];
    _size = 0;
    // rehash all entries
    for (DArrayIListEntry e : olddata) {
        while (e != null) {
            reinsert(e.key, e.value);
            e = e.next;
        }
    }
}