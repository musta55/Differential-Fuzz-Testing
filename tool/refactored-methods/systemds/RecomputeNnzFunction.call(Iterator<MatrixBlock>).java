@Override
public Iterator<Long> call(Iterator<MatrixBlock> iter) throws Exception {
    long nnz = 0;
    while (iter.hasNext()) {
        nnz += iter.next().getNonZeros();
    }
    return Arrays.asList(nnz).iterator();
}