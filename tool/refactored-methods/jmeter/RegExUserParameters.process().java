@Override
public void process() {
    if (log.isDebugEnabled()) {
        logDebug("Running up named: {}", getName());
    }
    Sampler entry = getThreadContext().getCurrentSampler();
    if (!(entry instanceof HTTPSamplerBase)) {
        return;
    }
    Map<String, String> paramMap = buildParamsMap();
    if (paramMap == null || paramMap.isEmpty()) {
        logInfo("Referenced RegExp was not found, no parameter will be changed");
        return;
    }
    HTTPSamplerBase sampler = (HTTPSamplerBase) entry;
    for (JMeterProperty jMeterProperty : sampler.getArguments()) {
        Argument arg = (Argument) jMeterProperty.getObjectValue();
        String oldValue = arg.getValue();
        String val = paramMap.get(arg.getName());
        if (val != null) {
            arg.setValue(val);
        }
        if (log.isDebugEnabled()) {
            logDebug("changed parameter: {} = {}, was: {}", arg.getName(), arg.getValue(), oldValue);
        }
    }
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

