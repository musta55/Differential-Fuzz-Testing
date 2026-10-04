/**
 * A new instance is created for each thread group, and the
 * clone() method is then called to create copies for each thread in a
 * thread group.
 */
@Override
public Object clone() {
    return (RegExUserParameters) super.clone();
}
// ---- helper method(s) introduced by the refactoring ----
private static boolean areRegexGroupsPresent(JMeterVariables jmvars, String regExRefName, String grNames, String grValues) {
    return jmvars.get(regExRefName + MATCH_NR) != null && jmvars.get(regExRefName + 1 + REGEX_GROUP_SUFFIX + grNames) != null && jmvars.get(regExRefName + 1 + REGEX_GROUP_SUFFIX + grValues) != null;
}

private void logDebug(String message, Object... args) {
    log.debug("RegExUserParameters element: {} => " + message, getName(), args);
}

private void logInfo(String message) {
    log.info("RegExUserParameters element: {} => " + message, getName());
}

