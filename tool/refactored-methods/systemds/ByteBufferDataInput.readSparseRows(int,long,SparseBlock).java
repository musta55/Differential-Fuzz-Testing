@Override
public long readSparseRows(int rlen, long nnz, SparseBlock rows) throws IOException {
    long gnnz = readAndProcessSparseRows(rlen, rows);
    //sanity check valid number of read nnz
    if (gnnz != nnz)
        throw new IOException("Invalid number of read nnz: " + gnnz + " vs " + nnz);
    return nnz;
}
// ---- helper method(s) introduced by the refactoring ----
private long readAndProcessSparseRows(int rlen, SparseBlock rows) throws IOException {
    long gnnz = 0;
    for (int i = 0; i < rlen; i++) {
        int lnnz = _buff.getInt();
        if (lnnz > 0) {
            //non-zero row
            //preallocate row
            rows.allocate(i, lnnz);
            readSingleSparseRow(i, lnnz, rows);
            gnnz += lnnz;
        }
    }
    return gnnz;
}

private void readSingleSparseRow(int rowIndex, int lnnz, SparseBlock rows) throws IOException {
    for (//read single sparse row
    int j = 0; //read single sparse row
    j < lnnz; //read single sparse row
    j++) rows.append(rowIndex, _buff.getInt(), _buff.getDouble());
}

