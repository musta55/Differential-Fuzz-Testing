@Override
public int getNumNonZerosInOffset(int idx) {
    int nz = 0;
    for (double v : getValues(idx)) nz += v == 0 ? 0 : 1;
    return nz;
}