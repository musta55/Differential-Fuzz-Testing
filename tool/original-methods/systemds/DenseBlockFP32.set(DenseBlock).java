@Override
public DenseBlock set(DenseBlock db) {
    // ToDo: Performance tests
    double[] data = db.valuesAt(0);
    for (int i = 0; i < _rlen * _odims[0]; i++) {
        _data[i] = (float) data[i];
    }
    return this;
}