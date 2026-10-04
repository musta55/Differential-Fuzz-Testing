@Override
public Iterator<Tuple2<TensorIndexes, TensorBlock>> call(Tuple2<TensorIndexes, TensorBlock> input) throws Exception {
    TensorIndexes indexes = input._1();
    TensorBlock tensorBlock = input._2();
    validateInput(indexes, tensorBlock);
    ArrayList<Tuple2<TensorIndexes, TensorBlock>> result = new ArrayList<>();
    long[] indexArray = indexes.getIndexes();
    for (int i = 1; i <= numberOfReplicas; i++) {
        indexArray[replicationDimension] = i;
        result.add(new Tuple2<>(new TensorIndexes(indexArray), tensorBlock));
    }
    return result.iterator();
}
// ---- helper method(s) introduced by the refactoring ----
private void validateInput(TensorIndexes indexes, TensorBlock tensorBlock) throws Exception {
    if (indexes.getIndex(replicationDimension) != 1 || (tensorBlock.getNumDims() > replicationDimension && tensorBlock.getDim(replicationDimension) > 1)) {
        throw new Exception("Expected dimension " + replicationDimension + " to be 1 in ReplicateTensor");
    }
}

