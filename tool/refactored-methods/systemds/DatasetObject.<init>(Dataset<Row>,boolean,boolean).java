public DatasetObject(Dataset<Row> dsvar, boolean isVector, boolean containsID) {
    super();
    this.datasetHandle = dsvar;
    this.isVectorBased = isVector;
    this.containsID = containsID;
}