@Override
public long readSparseRows(int rlen, long nnz, SparseBlock rows) throws IOException {
    //counter for non-zero elements
    long gnnz = 0;
    //read all individual sparse rows from input
    for (int i = 0; i < rlen; i++) {
        int lnnz = _buff.getInt();
        if (lnnz > 0) {
            //non-zero row
            //preallocate row
            rows.allocate(i, lnnz);
            for (//read single sparse row
            int j = 0; //read single sparse row
            j < lnnz; //read single sparse row
            j++) rows.append(i, _buff.getInt(), _buff.getDouble());
            gnnz += lnnz;
        }
    }
    //sanity check valid number of read nnz
    if (gnnz != nnz)
        throw new IOException("Invalid number of read nnz: " + gnnz + " vs " + nnz);
    return nnz;
}