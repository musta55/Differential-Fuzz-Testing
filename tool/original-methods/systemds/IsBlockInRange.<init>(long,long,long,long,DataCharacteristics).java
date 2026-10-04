public IsBlockInRange(long rl, long ru, long cl, long cu, DataCharacteristics mc) {
    _rl = rl;
    _ru = ru;
    _cl = cl;
    _cu = cu;
    _blen = mc.getBlocksize();
}