protected DataCharacteristics estimExactMetaData(DataCharacteristics dc1, DataCharacteristics dc2, OpCode op) {
    switch(op) {
        case EQZERO:
            return new MatrixCharacteristics(dc1.getRows(), dc1.getCols(), dc1.getRows() * dc1.getCols() - dc1.getNonZeros());
        case DIAG:
            return (dc1.getCols() == 1) ? new MatrixCharacteristics(dc1.getRows(), dc1.getRows(), dc1.getNonZeros()) : new MatrixCharacteristics(dc1.getRows(), 1, Math.min(dc1.getRows(), dc1.getNonZeros()));
        // binary operations that preserve sparsity exactly
        case CBIND:
            return new MatrixCharacteristics(dc1.getRows(), dc1.getCols() + dc2.getCols(), dc1.getNonZeros() + dc2.getNonZeros());
        case RBIND:
            return new MatrixCharacteristics(dc1.getRows() + dc2.getRows(), dc1.getCols(), dc1.getNonZeros() + dc2.getNonZeros());
        case TRANS:
            return new MatrixCharacteristics(dc1.getCols(), dc1.getRows(), dc1.getNonZeros());
        // unary operation that preserve sparsity exactly
        case NEQZERO:
        case RESHAPE:
            return dc1;
        default:
            throw new HopsException("Opcode is not an exact meta data operation: " + op.name());
    }
}