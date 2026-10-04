@Override
public Iterator<Tuple2<TensorIndexes, TensorBlock>> call(Tuple2<TensorIndexes, TensorBlock> arg0) throws Exception {
    TensorIndexes ti = arg0._1();
    TensorBlock tb = arg0._2();
    TensorCharacteristics tc = new TensorCharacteristics(tb.getLongDims(), (int) _newBlen);
    long[] tensorIndexes = new long[_numDims];
    for (int i = 0; i < tb.getNumDims(); i++) {
        tensorIndexes[i] = 1 + (ti.getIndex(i) - 1) * tc.getNumBlocks(i);
    }
    Arrays.fill(tensorIndexes, tb.getNumDims(), tensorIndexes.length, 1);
    long[] zeroBasedTensorIndexes = new long[tb.getNumDims()];
    Arrays.fill(zeroBasedTensorIndexes, 1);
    ArrayList<Tuple2<TensorIndexes, TensorBlock>> retVal = new ArrayList<>();
    long numBlocks = tc.getNumBlocks();
    int[] offsets = new int[tb.getNumDims()];
    for (int i = 0; i < numBlocks; i++) {
        int[] dims = new int[tb.getNumDims()];
        UtilFunctions.computeSliceInfo(tc, zeroBasedTensorIndexes, dims, offsets);
        TensorBlock outBlock;
        if (tb.isBasic())
            outBlock = new TensorBlock(tb.getValueType(), dims);
        else {
            ValueType[] schema = new ValueType[dims[1]];
            System.arraycopy(tb.getSchema(), offsets[1], schema, 0, dims[1]);
            outBlock = new TensorBlock(schema, dims);
        }
        tb.slice(offsets, outBlock);
        retVal.add(new Tuple2<>(new TensorIndexes(tensorIndexes), outBlock));
        UtilFunctions.computeNextTensorIndexes(tc, tensorIndexes);
        UtilFunctions.computeNextTensorIndexes(tc, zeroBasedTensorIndexes);
    }
    return retVal.iterator();
}