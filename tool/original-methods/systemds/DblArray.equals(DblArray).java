public final boolean equals(DblArray that) {
    if (hashCode() == that.hashCode()) {
        final double[] t = _arr;
        final double[] o = that._arr;
        for (int i = 0; i < t.length; i++) if (!Util.eq(t[i], o[i]))
            return false;
        return true;
    }
    return false;
}