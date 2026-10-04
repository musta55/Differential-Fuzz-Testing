private void resize() {
    DArrayIListEntry[] oldData = _data;
    _data = new DArrayIListEntry[_data.length * RESIZE_FACTOR];
    _size = 0;
    for (DArrayIListEntry e : oldData) {
        while (e != null) {
            reinsert(e.key, e.value);
            e = e.next;
        }
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void createNewEntry(int index, DblArray key, int value) {
    _data[index] = new DArrayIListEntry(new DblArray(key), value);
    _size++;
}

