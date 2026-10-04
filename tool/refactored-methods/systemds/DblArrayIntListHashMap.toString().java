@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.getClass().getSimpleName());
    sb.append("   ").append(_size);
    for (int i = 0; i < _data.length; i++) {
        DArrayIListEntry ent = _data[i];
        if (ent != null) {
            sb.append("\n");
            sb.append("[");
            ent.toString(sb);
            sb.append("]");
        }
    }
    return sb.toString();
}
// ---- helper method(s) introduced by the refactoring ----
private void createNewEntry(int index, DblArray key, int value) {
    _data[index] = new DArrayIListEntry(new DblArray(key), value);
    _size++;
}

