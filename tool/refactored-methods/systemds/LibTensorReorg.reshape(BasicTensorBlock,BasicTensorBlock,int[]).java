/**
 * CP reshape operation (single input, single output tensor)
 *
 * @param in input tensor
 * @param out output tensor
 * @param dims dimensions
 * @return output tensor
 */
public static BasicTensorBlock reshape(BasicTensorBlock in, BasicTensorBlock out, int[] dims) {
    validateReshapeDimensions(in, dims);
    //check for same dimensions
    if (Arrays.equals(in.getDims(), dims)) {
        //copy incl dims, nnz
        if (SHALLOW_COPY_REORG)
            out.copyShallow(in);
        else
            // TODO deep copy
            out.copy(in);
        return out;
    }
    // TODO eval sparse output
    out._sparse = false;
    //set output dimensions
    out._dims = dims;
    out._nnz = in._nnz;
    //core reshape (sparse or dense)
    if (!in.isSparse() && !out.isSparse())
        reshapeDense(in, out, dims);
    else if (in.isSparse() && out.isSparse())
        throw new NotImplementedException();
    else if (in.isSparse())
        throw new NotImplementedException();
    else
        throw new NotImplementedException();
    return out;
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

