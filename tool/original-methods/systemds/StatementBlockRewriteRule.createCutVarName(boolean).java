public static String createCutVarName(boolean fun) {
    return fun ? FUN_CUT_PREFIX + _seq.getNextID() : SB_CUT_PREFIX + _seq.getNextID();
}