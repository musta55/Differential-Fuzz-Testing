private void merge(IntArrayList a, int label) {
    // Pointer A
    int pA = a.size() - 1;
    // Pointer Previous
    int pP = currentFill - 1;
    currentFill += a.size();
    // Pointer new
    int pN = currentFill - 1;
    while (pP >= 0 && pA >= 0) {
        int vA = a.get(pA);
        int vP = _indexes[pP];
        if (vP > vA) {
            set(pN--, vP, _labels.getIndex(pP--));
        } else {
            set(pN--, vA, label);
            pA--;
        }
    }
    while (pA >= 0) set(pN--, a.get(pA--), label);
}
// ---- helper method(s) introduced by the refactoring ----
private void insertArray(IntArrayList array, int label) {
    if (currentFill == 0) {
        currentFill = array.size();
        for (int i = 0; i < currentFill; i++) set(i, array.get(i), label);
    } else
        merge(array, label);
}

