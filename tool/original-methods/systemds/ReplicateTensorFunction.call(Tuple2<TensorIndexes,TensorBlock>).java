@Override
public Iterator<Tuple2<TensorIndexes, TensorBlock>> call(Tuple2<TensorIndexes, TensorBlock> arg0) throws Exception {
    TensorIndexes ix = arg0._1();
    TensorBlock tb = arg0._2();
    //sanity check inputs
    if (ix.getIndex(_byDim) != 1 || (tb.getNumDims() > _byDim && tb.getDim(_byDim) > 1)) {
        throw new Exception("Expected dimension " + _byDim + " to be 1 in ReplicateTensor");
    }
    ArrayList<Tuple2<TensorIndexes, TensorBlock>> retVal = new ArrayList<>();
    long[] indexes = ix.getIndexes();
    for (int i = 1; i <= _numReplicas; i++) {
        indexes[_byDim] = i;
        retVal.add(new Tuple2<>(new TensorIndexes(indexes), tb));
    }
    return retVal.iterator();
}