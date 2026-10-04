@Override
public DataCharacteristics set(long[] dims, int blocksize, long nnz) {
    _dims = dims;
    _blocksize = blocksize;
    return this;
}