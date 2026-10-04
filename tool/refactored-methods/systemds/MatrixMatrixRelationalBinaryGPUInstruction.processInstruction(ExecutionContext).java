@Override
public void processInstruction(ExecutionContext ec) {
    GPUStatistics.incrementNoOfExecutedGPUInst();
    MatrixObject in1 = getMatrixInputForGPUInstruction(ec, _input1.getName());
    MatrixObject in2 = getMatrixInputForGPUInstruction(ec, _input2.getName());
    long[] dimensions = calculateResultDimensions(in1, in2);
    long rlen = dimensions[0];
    long clen = dimensions[1];
    ec.setMetaData(_output.getName(), (int) rlen, (int) clen);
    BinaryOperator bop = (BinaryOperator) _optr;
    LibMatrixCUDA.matrixMatrixRelational(ec, ec.getGPUContext(0), getExtendedOpcode(), in1, in2, _output.getName(), bop);
    ec.releaseMatrixInputForGPUInstruction(_input1.getName());
    ec.releaseMatrixInputForGPUInstruction(_input2.getName());
    ec.releaseMatrixOutputForGPUInstruction(_output.getName());
}
// ---- helper method(s) introduced by the refactoring ----
private long[] calculateResultDimensions(MatrixObject in1, MatrixObject in2) {
    long rlen1 = in1.getNumRows();
    long clen1 = in1.getNumColumns();
    long rlen2 = in2.getNumRows();
    long clen2 = in2.getNumColumns();
    long rlen = rlen1;
    long clen = clen1;
    if (rlen1 != rlen2 || clen1 != clen2) {
        rlen = Math.max(rlen1, rlen2);
        clen = Math.max(clen1, clen2);
    }
    return new long[] { rlen, clen };
}

