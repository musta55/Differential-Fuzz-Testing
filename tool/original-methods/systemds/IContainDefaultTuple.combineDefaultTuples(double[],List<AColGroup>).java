public static double[] combineDefaultTuples(double[] defTup, List<AColGroup> right) {
    final int l = defTup.length;
    final double[] out = new double[l * right.size() + l];
    System.arraycopy(defTup, 0, out, 0, l);
    for (int i = l, j = 0; j < right.size(); i += l, j++) {
        final IContainDefaultTuple dtg = (IContainDefaultTuple) right.get(j);
        System.arraycopy(dtg.getDefaultTuple(), 0, out, i, l);
    }
    return out;
}