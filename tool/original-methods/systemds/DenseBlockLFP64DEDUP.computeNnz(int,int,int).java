@Override
protected long computeNnz(int bix, int start, int length) {
    int nnz = 0;
    int row_start = (int) Math.floor(start / _odims[0]);
    int col_start = start % _odims[0];
    for (int i = 0; i < length; i++) {
        if (_data[row_start] == null) {
            i += _odims[0] - 1 - col_start;
            col_start = 0;
            row_start += 1;
            continue;
        }
        nnz += _data[row_start][col_start] != 0 ? 1 : 0;
        col_start += 1;
        if (col_start == _odims[0]) {
            col_start = 0;
            row_start += 1;
        }
    }
    return nnz;
}