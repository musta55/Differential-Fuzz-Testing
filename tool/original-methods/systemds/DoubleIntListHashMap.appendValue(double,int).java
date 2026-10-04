/**
 * Append value into the hashmap, but ignore all zero keys.
 *
 * @param key   The key to add the value to
 * @param value The value to add
 */
public void appendValue(double key, int value) {
    if (key == 0)
        return;
    int hash = hash(key);
    int ix = indexFor(hash, _data.length);
    if (_data[ix] == null) {
        IntArrayList lstPtr = new IntArrayList();
        lstPtr.appendValue(value);
        _data[ix] = new DIListEntry(key, lstPtr);
        _size++;
    } else {
        for (DIListEntry e = _data[ix]; e != null; e = e.next) {
            if (Util.eq(e.key, key)) {
                IntArrayList lstPtr = e.value;
                lstPtr.appendValue(value);
                break;
            } else if (e.next == null) {
                IntArrayList lstPtr = new IntArrayList();
                lstPtr.appendValue(value);
                // Swap to place the new value, in front.
                DIListEntry eOld = _data[ix];
                _data[ix] = new DIListEntry(key, lstPtr);
                _data[ix].next = eOld;
                _size++;
                break;
            }
        }
    }
    if (_size >= LOAD_FACTOR * _data.length)
        resize();
}