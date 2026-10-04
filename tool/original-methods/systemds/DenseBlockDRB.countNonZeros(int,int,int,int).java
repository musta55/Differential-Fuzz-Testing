@Override
public long countNonZeros(int rl, int ru, int ol, int ou) {
    long nnz = 0;
    if (ol == 0 && ou == _odims[0]) {
        //specific case: all cols
        nnz += computeNnz(0, rl * _odims[0], (ru - rl) * _odims[0]);
    } else {
        for (int i = rl, ix = rl * _odims[0]; i < ru; i++, ix += _odims[0]) nnz += computeNnz(0, ix + ol, ou - ol);
    }
    return nnz;
}