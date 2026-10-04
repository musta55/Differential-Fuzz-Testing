@Override
public int getNumNonZerosInOffset(int idx) {
    return (int) Arrays.stream(getValues(idx)).filter(v -> v != 0).count();
}