private String getOpcode() {
    if (_type != LixCacheType.NONE)
        return "mapLeftIndex";
    else
        return OPCODE;
}