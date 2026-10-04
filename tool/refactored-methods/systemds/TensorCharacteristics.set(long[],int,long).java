@Override
public DataCharacteristics set(long[] dims, int blocksize, long nnz) {
    _dims = dims;
    _blocksize = blocksize;
    _nnz = nnz;
    return this;
}