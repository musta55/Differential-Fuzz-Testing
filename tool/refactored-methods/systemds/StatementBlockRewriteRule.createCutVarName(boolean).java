public static String createCutVarName(boolean fun) {
    return (fun ? FUN_CUT_PREFIX : SB_CUT_PREFIX) + _seq.getNextID();
}