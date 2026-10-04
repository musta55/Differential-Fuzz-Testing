@Override
public DenseBlock set(int rl, int ru, int cl, int cu, double v) {
    if (cl == 0 && cu == _odims[0])
        fillBlock(0, rl * _odims[0], ru * _odims[0], v);
    else
        for (int i = rl, ix = rl * _odims[0]; i < ru; i++, ix += _odims[0]) fillBlock(0, ix + cl, ix + cu, v);
    return this;
}