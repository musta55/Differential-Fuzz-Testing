@Override
public void processInstruction(ExecutionContext ec) {
    // Read input tensors
    TensorBlock inBlock1 = ec.getTensorInput(input1.getName());
    TensorBlock inBlock2 = ec.getTensorInput(input2.getName());
    // Perform computation using input tensors, and produce the result tensor
    BinaryOperator bop = (BinaryOperator) _optr;
    TensorBlock retBlock = inBlock1.binaryOperations(bop, inBlock2, null);
    // Release the memory occupied by input matrices
    ec.releaseTensorInput(input1.getName(), input2.getName());
    // TODO Ensure right dense/sparse output representation (guarded by released input memory)
    // Attach result matrix with MatrixObject associated with output_name
    ec.setTensorOutput(output.getName(), retBlock);
}