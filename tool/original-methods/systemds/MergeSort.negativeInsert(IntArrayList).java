protected void negativeInsert(IntArrayList a) {
    final int label = _numLabels - 1;
    // Pointer A
    int pA = a.size() - 1;
    // Pointer Previous
    int pP = currentFill - 1;
    // From here on currentFill is no longer needed.
    // Pointer new
    int pN = _indexes.length - 1;
    int vA = a.get(pA);
    // Pointer to last index
    int vM = _numRows - 1;
    // While both old indexes have to be added and a have to be ignored.
    while (pP >= 0 && pA >= 0 && pN >= 0) {
        final int vP = _indexes[pP];
        vA = a.get(pA);
        if (vP == vM)
            set(pN--, vM, _labels.getIndex(pP--));
        else if (vA == vM)
            pA--;
        else
            set(pN--, vM, label);
        vM--;
    }
    // If there is no more indexes to ignore
    if (pA < 0) {
        // add all remaining indexes from other arrays
        while (pP >= 0 && pN >= 0) {
            final int vP = _indexes[pP];
            if (vP == vM)
                set(pN--, vM, _labels.getIndex(pP--));
            else
                set(pN--, vM, label);
            vM--;
        }
    } else {
        // skip all indexes in a.
        while (pN >= 0 && pA >= 0) {
            vA = a.get(pA);
            if (vA < vM)
                set(pN--, vM, label);
            else
                pA--;
            vM--;
        }
    }
    // Fill the rest with the default value.
    while (pN >= 0 && vM >= 0) set(pN--, vM--, label);
}