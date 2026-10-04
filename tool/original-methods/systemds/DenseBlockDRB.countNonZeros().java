@Override
public long countNonZeros() {
    return computeNnz(0, 0, _rlen * _odims[0]);
}