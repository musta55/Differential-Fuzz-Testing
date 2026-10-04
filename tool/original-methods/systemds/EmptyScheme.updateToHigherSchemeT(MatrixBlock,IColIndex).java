private ICLAScheme updateToHigherSchemeT(MatrixBlock data, IColIndex columns) {
    // try with const
    double[] vals = new double[cols.size()];
    for (int c = 0; c < cols.size(); c++) vals[c] = data.get(c, 0);
    return ConstScheme.create(columns, vals).updateT(data, columns);
}