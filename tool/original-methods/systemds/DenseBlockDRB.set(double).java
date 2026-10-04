@Override
public DenseBlock set(double v) {
    fillBlock(0, 0, _rlen * _odims[0], v);
    return this;
}