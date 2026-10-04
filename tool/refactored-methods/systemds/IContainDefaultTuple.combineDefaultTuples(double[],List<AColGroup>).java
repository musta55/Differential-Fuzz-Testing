public static double[] combineDefaultTuples(double[] defTup, List<AColGroup> right) {
    int length = defTup.length;
    double[] result = initializeResultArray(length, right.size());
    copyInitialTuple(defTup, result, length);
    appendTuplesFromRight(defTup, right, result, length);
    return result;
}
// ---- helper method(s) introduced by the refactoring ----
private static double[] initializeResultArray(int tupleLength, int numberOfGroups) {
    return new double[tupleLength * (numberOfGroups + 1)];
}

private static void copyInitialTuple(double[] defTup, double[] result, int length) {
    System.arraycopy(defTup, 0, result, 0, length);
}

private static void appendTuplesFromRight(double[] defTup, List<AColGroup> right, double[] result, int length) {
    for (int i = length, j = 0; j < right.size(); i += length, j++) {
        IContainDefaultTuple dtg = (IContainDefaultTuple) right.get(j);
        System.arraycopy(dtg.getDefaultTuple(), 0, result, i, length);
    }
}

