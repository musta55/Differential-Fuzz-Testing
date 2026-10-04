@Override
public double getSparsity() {
    return _nnz / (double) getLength();
}