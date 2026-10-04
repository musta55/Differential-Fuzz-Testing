/**
 * CP reshape operation (single input, single output tensor)
 *
 * @param in input tensor
 * @param out output tensor
 * @param dims dimensions
 * @return output tensor
 */
public static BasicTensorBlock reshape(BasicTensorBlock in, BasicTensorBlock out, int[] dims) {
    long length = 1;
    for (int dim : dims) {
        length *= dim;
    }
    int[] inDims = in.getDims();
    //check validity
    if (in.getLength() != length) {
        throw new DMLRuntimeException("Reshape tensor requires consistent numbers of input/output cells (" + Arrays.toString(inDims) + ", " + Arrays.toString(dims) + ").");
    }
    //check for same dimensions
    if (Arrays.equals(inDims, dims)) {
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