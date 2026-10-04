private String getOpcode() {
    switch(_op) {
        case SUM:
            return Opcodes.UCUMACKP.toString();
        case PROD:
            return Opcodes.UCUMACM.toString();
        case SUM_PROD:
            return Opcodes.UCUMACPM.toString();
        case MIN:
            return Opcodes.UCUMACMIN.toString();
        case MAX:
            return Opcodes.UCUMACMAX.toString();
        default:
            return null;
    }
}