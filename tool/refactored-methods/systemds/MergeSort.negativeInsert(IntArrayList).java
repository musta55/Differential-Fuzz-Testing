private void negativeInsert(IntArrayList a) {
    final int label = _numLabels - 1;
    // Pointer A
    int pA = a.size() - 1;
    // Pointer Previous
    int pP = currentFill - 1;
    // Pointer new
    int pN = _indexes.length - 1;
    // Pointer to last index
    int vM = _numRows - 1;
    while (pP >= 0 && pA >= 0 && pN >= 0) {
        int vP = _indexes[pP];
        int vA = a.get(pA);
        if (vP == vM)
            set(pN--, vM, _labels.getIndex(pP--));
        else if (vA == vM)
            pA--;
        else
            set(pN--, vM, label);
        vM--;
    }
    if (pA < 0) {
        while (pP >= 0 && pN >= 0) {
            int vP = _indexes[pP];
            if (vP == vM)
                set(pN--, vM, _labels.getIndex(pP--));
            else
                set(pN--, vM, label);
            vM--;
        }
    } else {
        while (pN >= 0 && pA >= 0) {
            int vA = a.get(pA);
            if (vA < vM)
                set(pN--, vM, label);
            else
                pA--;
            vM--;
        }
    }
    while (pN >= 0 && vM >= 0) set(pN--, vM--, label);
}
// ---- helper method(s) introduced by the refactoring ----
private void insertArray(IntArrayList array, int label) {
    if (currentFill == 0) {
        currentFill = array.size();
        for (int i = 0; i < currentFill; i++) set(i, array.get(i), label);
    } else
        merge(array, label);
}

