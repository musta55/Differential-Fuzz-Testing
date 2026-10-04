private void merge(IntArrayList a, int label) {
    // Pointer A
    int pA = a.size();
    // Pointer Previous
    int pP = currentFill;
    currentFill = pA + pP;
    // Pointer new
    int pN = currentFill - 1;
    // last element
    pA--;
    // last element
    pP--;
    int vA, vP;
    while (pP >= 0 && pA >= 0) {
        vA = a.get(pA);
        vP = _indexes[pP];
        if (vP > vA) {
            set(pN--, vP, _labels.getIndex(pP--));
        } else {
            set(pN--, vA, label);
            pA--;
        }
    }
    while (pA >= 0) set(pN--, a.get(pA--), label);
}