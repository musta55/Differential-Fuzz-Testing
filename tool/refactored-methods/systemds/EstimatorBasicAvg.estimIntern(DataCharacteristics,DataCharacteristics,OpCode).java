private DataCharacteristics estimIntern(DataCharacteristics dc1, DataCharacteristics dc2, OpCode op) {
    switch(op) {
        case MM:
            return handleMatrixMultiplication(dc1, dc2);
        case MULT:
            return handleElementwiseMultiplication(dc1, dc2);
        case PLUS:
            return handleAddition(dc1, dc2);
        case EQZERO:
        case DIAG:
        case CBIND:
        case RBIND:
        case NEQZERO:
        case TRANS:
        case RESHAPE:
            return estimExactMetaData(dc1, dc2, op);
        default:
            throw new NotImplementedException();
    }
}
// ---- helper method(s) introduced by the refactoring ----
private DataCharacteristics handleMatrixMultiplication(DataCharacteristics dc1, DataCharacteristics dc2) {
    return new MatrixCharacteristics(dc1.getRows(), dc2.getCols(), OptimizerUtils.getMatMultNnz(dc1.getSparsity(), dc2.getSparsity(), dc1.getRows(), dc1.getCols(), dc2.getCols(), false));
}

private DataCharacteristics handleElementwiseMultiplication(DataCharacteristics dc1, DataCharacteristics dc2) {
    return new MatrixCharacteristics(dc1.getRows(), dc1.getCols(), OptimizerUtils.getNnz(dc1.getRows(), dc1.getCols(), dc1.getSparsity() * dc2.getSparsity()));
}

private DataCharacteristics handleAddition(DataCharacteristics dc1, DataCharacteristics dc2) {
    return new MatrixCharacteristics(dc1.getRows(), dc1.getCols(), OptimizerUtils.getNnz(dc1.getRows(), dc1.getCols(), dc1.getSparsity() + dc2.getSparsity() - dc1.getSparsity() * dc2.getSparsity()));
}

