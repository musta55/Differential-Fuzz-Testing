public int getLoops() {
    // Evaluation occurs when nbLoops is not yet evaluated
    // or when nbLoops is equal to special value INFINITE_LOOP_COUNT
    if (// No evaluated yet
    nbLoops == null || // Last iteration led to nbLoops == 0,
    nbLoops == 0 || // in this case as resetLoopCount will not be called,
    // it leads to no further evaluations if we don't evaluate, see BUG 56276
    nbLoops == // Number of iteration is set to infinite
    INFINITE_LOOP_COUNT) {
        try {
            nbLoops = get(getSchema().getLoops());
        } catch (NumberFormatException e) {
            nbLoops = 0;
        }
    }
    return nbLoops;
}