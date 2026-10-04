@Override
public Iterator<Tuple2<TensorIndexes, TensorBlock>> call(Tuple2<TensorIndexes, TensorBlock> arg0) throws Exception {
    TensorIndexes ti = arg0._1();
    TensorBlock tb = arg0._2();
    TensorCharacteristics tc = new TensorCharacteristics(tb.getLongDims(), (int) _newBlen);
    long[] tensorIndexes = initializeTensorIndexes(ti, tc);
    long[] zeroBasedTensorIndexes = initializeZeroBasedTensorIndexes();
    ArrayList<Tuple2<TensorIndexes, TensorBlock>> retVal = new ArrayList<>();
    long numBlocks = tc.getNumBlocks();
    int[] offsets = new int[tb.getNumDims()];
    for (int i = 0; i < numBlocks; i++) {
        int[] dims = new int[tb.getNumDims()];
        UtilFunctions.computeSliceInfo(tc, zeroBasedTensorIndexes, dims, offsets);
        TensorBlock outBlock = createOutputBlock(tb, dims, offsets);
        tb.slice(offsets, outBlock);
        retVal.add(new Tuple2<>(new TensorIndexes(tensorIndexes), outBlock));
        updateTensorIndexes(tc, tensorIndexes, zeroBasedTensorIndexes);
    }
    return retVal.iterator();
}
// ---- helper method(s) introduced by the refactoring ----
private long[] initializeTensorIndexes(TensorIndexes ti, TensorCharacteristics tc) {
    long[] tensorIndexes = new long[_numDims];
    for (int i = 0; i < ti.getNumDims(); i++) {
        tensorIndexes[i] = 1 + (ti.getIndex(i) - 1) * tc.getNumBlocks(i);
    }
    Arrays.fill(tensorIndexes, ti.getNumDims(), tensorIndexes.length, 1);
    return tensorIndexes;
}

private long[] initializeZeroBasedTensorIndexes() {
    long[] zeroBasedTensorIndexes = new long[_numDims];
    Arrays.fill(zeroBasedTensorIndexes, 1);
    return zeroBasedTensorIndexes;
}

private TensorBlock createOutputBlock(TensorBlock tb, int[] dims, int[] offsets) {
    if (tb.isBasic()) {
        return new TensorBlock(tb.getValueType(), dims);
    } else {
        ValueType[] schema = new ValueType[dims[1]];
        System.arraycopy(tb.getSchema(), offsets[1], schema, 0, dims[1]);
        return new TensorBlock(schema, dims);
    }
}

private void updateTensorIndexes(TensorCharacteristics tc, long[] tensorIndexes, long[] zeroBasedTensorIndexes) {
    UtilFunctions.computeNextTensorIndexes(tc, tensorIndexes);
    UtilFunctions.computeNextTensorIndexes(tc, zeroBasedTensorIndexes);
}

