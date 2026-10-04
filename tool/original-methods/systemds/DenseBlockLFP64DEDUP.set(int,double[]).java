@Override
public DenseBlock set(int r, double[] v) {
    if (v.length == _odims[0])
        _data[r] = v;
    else
        throw new RuntimeException("set Denseblock called with an array length [" + v.length + "], array to overwrite is of length [" + _odims[0] + "]");
    return this;
}