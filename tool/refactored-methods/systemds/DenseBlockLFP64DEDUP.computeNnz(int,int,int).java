@Override
protected long computeNnz(int bix, int start, int length) {
    int nnz = 0;
    int rowStart = start / _odims[0];
    int colStart = start % _odims[0];
    for (int i = 0; i < length; i++) {
        if (_data[rowStart] == null) {
            i += _odims[0] - 1 - colStart;
            colStart = 0;
            rowStart++;
            continue;
        }
        nnz += _data[rowStart][colStart] != 0 ? 1 : 0;
        colStart++;
        if (colStart == _odims[0]) {
            colStart = 0;
            rowStart++;
        }
    }
    return nnz;
}