private void insertWithNegative() {
    for (int i = 0; i < _offsets.length; i++) {
        if (i < _negativeIndex)
            insertArray(_offsets[i], i);
        else if (i > _negativeIndex)
            insertArray(_offsets[i], i - 1);
    }
    negativeInsert(_offsets[_negativeIndex]);
}
// ---- helper method(s) introduced by the refactoring ----
private void insertArray(IntArrayList array, int label) {
    if (currentFill == 0) {
        currentFill = array.size();
        for (int i = 0; i < currentFill; i++) set(i, array.get(i), label);
    } else
        merge(array, label);
}

