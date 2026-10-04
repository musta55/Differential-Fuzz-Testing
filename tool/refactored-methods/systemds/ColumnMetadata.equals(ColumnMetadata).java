public boolean equals(ColumnMetadata that) {
    return _ndistinct == that._ndistinct && (_mvValue == that._mvValue || _mvValue != null && _mvValue.equals(that._mvValue));
}