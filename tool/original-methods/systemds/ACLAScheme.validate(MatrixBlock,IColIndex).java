private final void validate(MatrixBlock data, IColIndex columns) throws IllegalArgumentException {
    if (columns.size() != cols.size())
        throw new IllegalArgumentException("Invalid number of columns to encode expected: " + cols.size() + " but got: " + columns.size());
    final int nCol = data.getNumColumns();
    if (nCol < cols.get(cols.size() - 1))
        throw new IllegalArgumentException("Invalid columns to encode with max col:" + nCol + " list of columns: " + columns);
}