@Override
public int countNonZeros(int r) {
    return (int) computeNnz(0, r * _odims[0], _odims[0]);
}