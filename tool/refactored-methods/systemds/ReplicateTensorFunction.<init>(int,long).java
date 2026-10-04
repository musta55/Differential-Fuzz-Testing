public ReplicateTensorFunction(int replicationDimension, long numberOfReplicas) {
    this.replicationDimension = replicationDimension;
    this.numberOfReplicas = numberOfReplicas;
}
// ---- helper method(s) introduced by the refactoring ----
private void validateInput(TensorIndexes indexes, TensorBlock tensorBlock) throws Exception {
    if (indexes.getIndex(replicationDimension) != 1 || (tensorBlock.getNumDims() > replicationDimension && tensorBlock.getDim(replicationDimension) > 1)) {
        throw new Exception("Expected dimension " + replicationDimension + " to be 1 in ReplicateTensor");
    }
}

