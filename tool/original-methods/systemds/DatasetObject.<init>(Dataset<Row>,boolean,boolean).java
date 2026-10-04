public DatasetObject(Dataset<Row> dsvar, boolean isVector, boolean containsID) {
    super();
    _dsHandle = dsvar;
    _isVector = isVector;
    _containsID = containsID;
}