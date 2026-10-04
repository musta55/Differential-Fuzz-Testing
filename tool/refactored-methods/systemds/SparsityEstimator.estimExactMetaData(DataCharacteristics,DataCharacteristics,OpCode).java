protected DataCharacteristics estimExactMetaData(DataCharacteristics dc1, DataCharacteristics dc2, OpCode op) {
    switch(op) {
        case EQZERO:
            return estimEqZero(dc1);
        case DIAG:
            return estimDiag(dc1);
        case CBIND:
            return estimCBind(dc1, dc2);
        case RBIND:
            return estimRBind(dc1, dc2);
        case TRANS:
            return estimTrans(dc1);
        case NEQZERO:
        case RESHAPE:
            return dc1;
        default:
            throw new HopsException("Opcode is not an exact meta data operation: " + op.name());
    }
}
// ---- helper method(s) introduced by the refactoring ----
private DataCharacteristics estimEqZero(DataCharacteristics dc1) {
    return new MatrixCharacteristics(dc1.getRows(), dc1.getCols(), dc1.getRows() * dc1.getCols() - dc1.getNonZeros());
}

private DataCharacteristics estimDiag(DataCharacteristics dc1) {
    return (dc1.getCols() == 1) ? new MatrixCharacteristics(dc1.getRows(), dc1.getRows(), dc1.getNonZeros()) : new MatrixCharacteristics(dc1.getRows(), 1, Math.min(dc1.getRows(), dc1.getNonZeros()));
}

private DataCharacteristics estimCBind(DataCharacteristics dc1, DataCharacteristics dc2) {
    return new MatrixCharacteristics(dc1.getRows(), dc1.getCols() + dc2.getCols(), dc1.getNonZeros() + dc2.getNonZeros());
}

private DataCharacteristics estimRBind(DataCharacteristics dc1, DataCharacteristics dc2) {
    return new MatrixCharacteristics(dc1.getRows() + dc2.getRows(), dc1.getCols(), dc1.getNonZeros() + dc2.getNonZeros());
}

private DataCharacteristics estimTrans(DataCharacteristics dc1) {
    return new MatrixCharacteristics(dc1.getCols(), dc1.getRows(), dc1.getNonZeros());
}

