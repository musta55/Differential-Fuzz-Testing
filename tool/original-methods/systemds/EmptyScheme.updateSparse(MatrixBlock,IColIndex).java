private ICLAScheme updateSparse(MatrixBlock data, IColIndex columns) {
    final SparseBlock sb = data.getSparseBlock();
    final int nRow = data.getNumRows();
    if (columns.size() == 1) {
        final int col = columns.get(0);
        for (int i = 0; i < nRow; i++) {
            if (sb.get(i, col) == 0.0)
                return updateToHigherScheme(data, columns);
        }
    } else if (columns.size() * 2 > data.getNumColumns()) {
        // if we have many many columns
        for (int i = 0; i < nRow; i++) {
            int apos = sb.pos(i);
            final int alen = sb.size(i) + apos;
            final int[] aix = sb.indexes(i);
            int offC = 0;
            while (apos < alen || offC < columns.size()) {
                int va = aix[apos];
                int vb = columns.get(offC);
                if (va < vb)
                    apos++;
                else if (vb < va)
                    offC++;
                else if (va == vb)
                    return updateToHigherScheme(data, columns);
            }
        }
    } else {
        for (int i = 0; i < nRow; i++) {
            for (int j = 0; i < columns.size(); j++) {
                final int col = columns.get(j);
                if (sb.get(i, col) == 0.0)
                    return updateToHigherScheme(data, columns);
            }
        }
    }
    return this;
}