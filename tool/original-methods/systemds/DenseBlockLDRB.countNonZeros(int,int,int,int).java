@Override
public long countNonZeros(int rl, int ru, int cl, int cu) {
    long nnz = 0;
    boolean allColumns = cl == 0 && cu == _odims[0];
    int rb = pos(rl);
    int re = blockSize() * _odims[0];
    // loop over rows of blocks, and call computeNnz for the specified columns
    for (int bi = index(rl); bi <= index(ru - 1); bi++) {
        // loop complete block if not last one
        if (bi == index(ru - 1)) {
            re = pos(ru - 1) + _odims[0];
        }
        if (allColumns) {
            nnz += computeNnz(bi, rb, re - rb);
        } else {
            for (int ri = rb; ri < re; ri += _odims[0]) {
                nnz += computeNnz(bi, ri + cl, cu - cl);
            }
        }
        rb = 0;
    }
    return nnz;
}