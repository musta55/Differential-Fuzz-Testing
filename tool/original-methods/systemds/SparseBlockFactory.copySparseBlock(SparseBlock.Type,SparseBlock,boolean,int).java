public static SparseBlock copySparseBlock(SparseBlock.Type type, SparseBlock sblock, boolean forceCopy, int clen) {
    //sanity check for empty inputs
    if (sblock == null)
        return null;
    //check for existing target type
    if (!forceCopy && isSparseBlockType(sblock, type)) {
        return sblock;
    }
    //create target sparse block
    switch(type) {
        case MCSR:
            return new SparseBlockMCSR(sblock);
        case CSR:
            return new SparseBlockCSR(sblock);
        case COO:
            return new SparseBlockCOO(sblock);
        case DCSR:
            return new SparseBlockDCSR(sblock);
        case MCSC:
            return new SparseBlockMCSC(sblock, clen);
        case CSC:
            return new SparseBlockCSC(sblock, clen);
        default:
            throw new RuntimeException("Unexpected sparse block type: " + type.toString());
    }
}