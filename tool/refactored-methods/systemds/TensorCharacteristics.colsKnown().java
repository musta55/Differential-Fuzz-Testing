@Override
public boolean colsKnown() {
    return _dims.length > 1 && _dims[1] >= 0;
}