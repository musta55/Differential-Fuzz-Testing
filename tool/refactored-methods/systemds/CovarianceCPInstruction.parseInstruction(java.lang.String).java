public static CovarianceCPInstruction parseInstruction(String str) {
    String[] parts = InstructionUtils.getInstructionPartsWithValueType(str);
    String opcode = parts[0];
    if (!opcode.equalsIgnoreCase(Opcodes.COV.toString())) {
        throw new DMLRuntimeException("CovarianceCPInstruction.parseInstruction():: Unknown opcode " + opcode);
    }
    // w/o opcode
    InstructionUtils.checkNumFields(parts, 4, 5);
    CPOperand in1 = new CPOperand(parts[1]);
    CPOperand in2 = new CPOperand(parts[2]);
    CPOperand in3 = (parts.length == 5) ? null : new CPOperand(parts[3]);
    CPOperand out = new CPOperand(parts[parts.length - 2]);
    int numThreads = Integer.parseInt(parts[parts.length - 1]);
    COVOperator cov = new COVOperator(COV.getCOMFnObject(), numThreads);
    return new Builder().operator(cov).input1(in1).input2(in2).input3(in3).output(out).opcode(opcode).instructionString(str).build();
}
// ---- helper method(s) introduced by the refactoring ----
private CovarianceCPInstruction(Builder builder) {
    super(CPType.AggregateBinary, builder.operator, builder.input1, builder.input2, builder.input3, builder.output, builder.opcode, builder.instructionString);
}

private CmCovObject performCovarianceOperation(ExecutionContext ec, MatrixBlock matBlock1, MatrixBlock matBlock2, COVOperator covOp) {
    if (input3 == null) {
        return performUnweightedCovariance(ec, matBlock1, matBlock2, covOp);
    } else {
        return performWeightedCovariance(ec, matBlock1, matBlock2, covOp);
    }
}

private CmCovObject performUnweightedCovariance(ExecutionContext ec, MatrixBlock matBlock1, MatrixBlock matBlock2, COVOperator covOp) {
    CmCovObject covObj = matBlock1.covOperations(covOp, matBlock2);
    ec.releaseMatrixInput(input1.getName(), input2.getName());
    return covObj;
}

private CmCovObject performWeightedCovariance(ExecutionContext ec, MatrixBlock matBlock1, MatrixBlock matBlock2, COVOperator covOp) {
    MatrixBlock wtBlock = ec.getMatrixInput(input3.getName());
    CmCovObject covObj = matBlock1.covOperations(covOp, matBlock2, wtBlock);
    ec.releaseMatrixInput(input1.getName(), input2.getName(), input3.getName());
    return covObj;
}

