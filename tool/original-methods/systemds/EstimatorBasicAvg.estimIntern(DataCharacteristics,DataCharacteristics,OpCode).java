private DataCharacteristics estimIntern(DataCharacteristics dc1, DataCharacteristics dc2, OpCode op) {
    switch(op) {
        case MM:
            return new MatrixCharacteristics(dc1.getRows(), dc2.getCols(), OptimizerUtils.getMatMultNnz(dc1.getSparsity(), dc2.getSparsity(), dc1.getRows(), dc1.getCols(), dc2.getCols(), false));
        case MULT:
            return new MatrixCharacteristics(dc1.getRows(), dc1.getCols(), OptimizerUtils.getNnz(dc1.getRows(), dc1.getCols(), dc1.getSparsity() * dc2.getSparsity()));
        case PLUS:
            return new MatrixCharacteristics(dc1.getRows(), dc1.getCols(), OptimizerUtils.getNnz(dc1.getRows(), dc1.getCols(), dc1.getSparsity() + dc2.getSparsity() - dc1.getSparsity() * dc2.getSparsity()));
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