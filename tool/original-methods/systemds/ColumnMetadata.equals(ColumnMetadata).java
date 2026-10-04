public boolean equals(ColumnMetadata that) {
    if (that._ndistinct != this._ndistinct)
        return false;
    else if (that._mvValue == null)
        return this._mvValue == null;
    else if (this._mvValue == null)
        return false;
    else
        return that._mvValue.equals(this._mvValue);
}