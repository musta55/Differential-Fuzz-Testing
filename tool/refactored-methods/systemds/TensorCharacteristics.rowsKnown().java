@Override
public boolean rowsKnown() {
    return _dims.length > 0 && _dims[0] >= 0;
}