@Override
public boolean isUltraSparse() {
    return getSparsity() < 0.01;
}