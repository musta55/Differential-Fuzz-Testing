private ICLAScheme updateToHigherScheme(MatrixBlock data, IColIndex columns) {
    // try with const
    double[] vals = new double[cols.size()];
    for (int c = 0; c < cols.size(); c++) vals[c] = data.get(0, c);
    return ConstScheme.create(columns, vals).update(data, columns);
}