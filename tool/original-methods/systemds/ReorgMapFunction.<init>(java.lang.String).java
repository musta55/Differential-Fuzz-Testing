public ReorgMapFunction(String opcode) {
    if (opcode.equalsIgnoreCase(Opcodes.TRANSPOSE.toString())) {
        _indexFnObject = SwapIndex.getSwapIndexFnObject();
    } else if (opcode.equalsIgnoreCase(Opcodes.DIAG.toString())) {
        //diagM2V
        _indexFnObject = DiagIndex.getDiagIndexFnObject(false);
    } else {
        throw new DMLRuntimeException("Incorrect opcode for RDDReorgMapFunction:" + opcode);
    }
    _reorgOp = new ReorgOperator(_indexFnObject);
}