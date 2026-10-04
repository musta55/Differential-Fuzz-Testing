@Override
public double[] values(int r) {
    if (_data[r] == null) {
        allocateBlock(r, _odims[0]);
    }
    return _data[r];
}