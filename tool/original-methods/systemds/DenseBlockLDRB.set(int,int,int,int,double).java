@Override
public DenseBlock set(int rl, int ru, int cl, int cu, double v) {
    boolean allColumns = cl == 0 && cu == _odims[0];
    int rb = pos(rl);
    int re = blockSize() * _odims[0];
    for (int bi = index(rl); bi <= index(ru - 1); bi++) {
        if (bi == index(ru - 1)) {
            re = pos(ru - 1) + _odims[0];
        }
        if (allColumns) {
            fillBlock(bi, rb, re, v);
        } else {
            for (int ri = rb; ri < re; ri += _odims[0]) {
                fillBlock(bi, ri + cl, ri + cu, v);
            }
        }
        rb = 0;
    }
    return this;
}