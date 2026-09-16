private Map<String, String> buildParamsMap() {
    String regExRefName = getRegExRefName() + "_";
    String grNames = getRegParamNamesGrNr();
    String grValues = getRegExParamValuesGrNr();
    JMeterVariables jmvars = getThreadContext().getVariables();
    if (!areRegexGroupsPresent(jmvars, regExRefName, grNames, grValues)) {
        return null;
    }
    int n = Integer.parseInt(jmvars.get(regExRefName + MATCH_NR));
    Map<String, String> map = new HashMap<>(n);
    for (int i = 1; i <= n; i++) {
        map.put(jmvars.get(regExRefName + i + REGEX_GROUP_SUFFIX + grNames), jmvars.get(regExRefName + i + REGEX_GROUP_SUFFIX + grValues));
    }
    return map;
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

