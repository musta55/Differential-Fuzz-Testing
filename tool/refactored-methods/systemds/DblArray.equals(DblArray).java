public final boolean equals(DblArray that) {
    if (hashCode() != that.hashCode())
        return false;
    return Arrays.equals(_arr, that._arr);
}