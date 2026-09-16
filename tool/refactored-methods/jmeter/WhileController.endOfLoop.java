private boolean endOfLoop(boolean loopEnd) {
    if (breakLoop) {
        return true;
    }
    String cnd = getCondition().trim();
    return evaluateCondition(cnd, loopEnd);
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Evaluate the condition, which can be:
 * blank or LAST = was the last sampler OK?
 * otherwise, evaluate the condition to see if it is not "false"
 * If blank, only evaluate at the end of the loop
 *
 * Must only be called at start and end of loop
 *
 * @param loopEnd - are we at loop end?
 * @return true means end of loop has been reached
 */
private static boolean evaluateCondition(String cnd, boolean loopEnd) {
    log.debug("Condition string: '{}'", cnd);
    boolean res;
    // If blank, only check previous sample when at end of loop
    if ((loopEnd && cnd.isEmpty()) || "LAST".equalsIgnoreCase(cnd)) {
        // $NON-NLS-1$
        JMeterVariables threadVars = JMeterContextService.getContext().getVariables();
        // $NON-NLS-1$
        res = "false".equalsIgnoreCase(threadVars.get(JMeterThread.LAST_SAMPLE_OK));
    } else {
        // cnd may be null if next() called us
        // $NON-NLS-1$
        res = "false".equalsIgnoreCase(cnd);
    }
    log.debug("Condition value: '{}'", res);
    return res;
}

