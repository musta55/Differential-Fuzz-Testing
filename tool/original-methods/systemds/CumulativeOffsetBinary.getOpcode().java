private String getOpcode() {
    switch(_op) {
        case SUM:
            return Opcodes.BCUMOFFKP.toString();
        case PROD:
            return Opcodes.BCUMOFFM.toString();
        case SUM_PROD:
            return Opcodes.BCUMOFFPM.toString();
        case MIN:
            return Opcodes.BCUMOFFMIN.toString();
        case MAX:
            return Opcodes.BCUMOFFMAX.toString();
        default:
            return null;
    }
}