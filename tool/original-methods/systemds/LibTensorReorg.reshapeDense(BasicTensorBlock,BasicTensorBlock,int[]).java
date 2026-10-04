private static void reshapeDense(BasicTensorBlock in, BasicTensorBlock out, int[] dims) {
    //reshape empty block
    if (in._denseBlock == null)
        return;
    //shallow dense by-row reshape (w/o result allocation)
    if (SHALLOW_COPY_REORG && in._denseBlock.numBlocks() == 1) {
        //since the physical representation of dense matrices is always the same,
        //we don't need to create a copy, given our copy on write semantics.
        //however, note that with update in-place this would be an invalid optimization
        DenseBlock denseBlock = in._denseBlock;
        if (denseBlock instanceof DenseBlockBool) {
            DenseBlockBool specificBlock = (DenseBlockBool) denseBlock;
            out._denseBlock = DenseBlockFactory.createDenseBlock(specificBlock.getData(), dims);
        } else if (denseBlock instanceof DenseBlockString) {
            DenseBlockString specificBlock = (DenseBlockString) denseBlock;
            out._denseBlock = DenseBlockFactory.createDenseBlock(specificBlock.getData(), dims);
        } else if (denseBlock instanceof DenseBlockFP64) {
            out._denseBlock = DenseBlockFactory.createDenseBlock(in._denseBlock.valuesAt(0), dims);
        } else if (denseBlock instanceof DenseBlockFP32) {
            DenseBlockFP32 specificBlock = (DenseBlockFP32) denseBlock;
            out._denseBlock = DenseBlockFactory.createDenseBlock(specificBlock.getData(), dims);
        } else if (denseBlock instanceof DenseBlockInt64) {
            DenseBlockInt64 specificBlock = (DenseBlockInt64) denseBlock;
            out._denseBlock = DenseBlockFactory.createDenseBlock(specificBlock.getData(), dims);
        } else if (denseBlock instanceof DenseBlockInt32) {
            DenseBlockInt32 specificBlock = (DenseBlockInt32) denseBlock;
            out._denseBlock = DenseBlockFactory.createDenseBlock(specificBlock.getData(), dims);
        }
        return;
    }
    out.set(in);
}