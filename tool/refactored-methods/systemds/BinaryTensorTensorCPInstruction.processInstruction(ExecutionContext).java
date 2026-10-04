@Override
public void processInstruction(ExecutionContext ec) {
    // Read input tensors
    TensorBlock inBlock1 = ec.getTensorInput(config.in1.getName());
    TensorBlock inBlock2 = ec.getTensorInput(config.in2.getName());
    // Perform computation using input tensors, and produce the result tensor
    BinaryOperator bop = (BinaryOperator) config.op;
    TensorBlock retBlock = inBlock1.binaryOperations(bop, inBlock2, null);
    // Release the memory occupied by input matrices
    ec.releaseTensorInput(config.in1.getName(), config.in2.getName());
    // TODO Ensure right dense/sparse output representation (guarded by released input memory)
    // Attach result matrix with MatrixObject associated with output_name
    ec.setTensorOutput(config.out.getName(), retBlock);
}
// ---- helper method(s) introduced by the refactoring ----
protected BinaryTensorTensorCPInstruction(Config config) {
    super(CPType.Binary, config.op, config.in1, config.in2, config.out, config.opcode, config.istr);
    this.config = config;
}

