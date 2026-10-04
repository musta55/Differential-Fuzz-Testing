public static boolean isValidParamName(String key) {
    return Arrays.asList(WRITE_VALID_PARAM_NAMES).contains(key);
}
// ---- helper method(s) introduced by the refactoring ----
private OutputStatement createDeepCopy(String prefix) {
    return new OutputStatement(null, Expression.DataOp.WRITE, this);
}

