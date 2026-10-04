private static void reshapeDense(BasicTensorBlock in, BasicTensorBlock out, int[] dims) {
    if (in._denseBlock == null)
        return;
    if (SHALLOW_COPY_REORG && in._denseBlock.numBlocks() == 1) {
        out._denseBlock = createDenseBlockFrom(in._denseBlock, dims);
        return;
    }
    out.set(in);
}
// ---- helper method(s) introduced by the refactoring ----
private static void validateReshapeDimensions(BasicTensorBlock in, int[] dims) {
    long length = 1;
    for (int dim : dims) {
        length *= dim;
    }
    if (in.getLength() != length) {
        throw new DMLRuntimeException("Reshape tensor requires consistent numbers of input/output cells (" + Arrays.toString(in.getDims()) + ", " + Arrays.toString(dims) + ").");
    }
}

private static DenseBlock createDenseBlockFrom(DenseBlock denseBlock, int[] dims) {
    if (denseBlock instanceof DenseBlockBool) {
        return DenseBlockFactory.createDenseBlock(((DenseBlockBool) denseBlock).getData(), dims);
    } else if (denseBlock instanceof DenseBlockString) {
        return DenseBlockFactory.createDenseBlock(((DenseBlockString) denseBlock).getData(), dims);
    } else if (denseBlock instanceof DenseBlockFP64) {
        return DenseBlockFactory.createDenseBlock(denseBlock.valuesAt(0), dims);
    } else if (denseBlock instanceof DenseBlockFP32) {
        return DenseBlockFactory.createDenseBlock(((DenseBlockFP32) denseBlock).getData(), dims);
    } else if (denseBlock instanceof DenseBlockInt64) {
        return DenseBlockFactory.createDenseBlock(((DenseBlockInt64) denseBlock).getData(), dims);
    } else if (denseBlock instanceof DenseBlockInt32) {
        return DenseBlockFactory.createDenseBlock(((DenseBlockInt32) denseBlock).getData(), dims);
    }
    // unreachable due to previous checks
    return null;
}

