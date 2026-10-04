@Override
public void processInstruction(ExecutionContext ec) {
    GPUStatistics.incrementNoOfExecutedGPUInst();
    MatrixObject in1 = getMatrixInputForGPUInstruction(ec, _input1.getName());
    MatrixObject in2 = getMatrixInputForGPUInstruction(ec, _input2.getName());
    //TODO: make hop level changes for this
    boolean isLeftTransposed = false;
    boolean isRightTransposed = false;
    long rlen1 = in1.getNumRows();
    long clen1 = in1.getNumColumns();
    long rlen2 = in2.getNumRows();
    long clen2 = in2.getNumColumns();
    // Assume ordinary binary op
    long rlen = rlen1;
    long clen = clen1;
    // Outer binary op ( [100,1] + [1,100] or [100,100] + [100,1]
    if (rlen1 != rlen2 || clen1 != clen2) {
        rlen = rlen1 > rlen2 ? rlen1 : rlen2;
        clen = clen1 > clen2 ? clen1 : clen2;
    }
    ec.setMetaData(_output.getName(), (int) rlen, (int) clen);
    BinaryOperator bop = (BinaryOperator) _optr;
    LibMatrixCUDA.matrixMatrixArithmetic(ec, ec.getGPUContext(0), getExtendedOpcode(), in1, in2, _output.getName(), isLeftTransposed, isRightTransposed, bop);
    ec.releaseMatrixInputForGPUInstruction(_input1.getName());
    ec.releaseMatrixInputForGPUInstruction(_input2.getName());
    ec.releaseMatrixOutputForGPUInstruction(_output.getName());
}