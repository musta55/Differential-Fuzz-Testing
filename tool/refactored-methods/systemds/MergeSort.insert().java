private void insert() {
    for (int i = 0; i < _offsets.length; i++) insertArray(_offsets[i], i);
}
// ---- helper method(s) introduced by the refactoring ----
private void insertArray(IntArrayList array, int label) {
    if (currentFill == 0) {
        currentFill = array.size();
        for (int i = 0; i < currentFill; i++) set(i, array.get(i), label);
    } else
        merge(array, label);
}

