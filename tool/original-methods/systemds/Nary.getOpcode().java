private String getOpcode() {
    switch(operationType) {
        case PRINTF:
        case CBIND:
        case RBIND:
        case EVAL:
        case LIST:
        case EINSUM:
            return operationType.name().toLowerCase();
        case MIN:
        case MAX:
            //need to differentiate from binary min/max operations
            return "n" + operationType.name().toLowerCase();
        case PLUS:
            return Opcodes.NP.toString();
        case MULT:
            return Opcodes.NM.toString();
        default:
            throw new UnsupportedOperationException("Nary operation type (" + operationType + ") is not defined.");
    }
}