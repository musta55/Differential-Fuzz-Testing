private Map<String, String> buildParamsMap() {
    String regExRefName = getRegExRefName() + "_";
    String grNames = getRegParamNamesGrNr();
    String grValues = getRegExParamValuesGrNr();
    JMeterVariables jmvars = getThreadContext().getVariables();
    // verify if regex groups exists
    if (jmvars.get(regExRefName + MATCH_NR) == null || jmvars.get(regExRefName + 1 + REGEX_GROUP_SUFFIX + grNames) == null || jmvars.get(regExRefName + 1 + REGEX_GROUP_SUFFIX + grValues) == null) {
        return null;
    }
    int n = Integer.parseInt(jmvars.get(regExRefName + MATCH_NR));
    Map<String, String> map = new HashMap<>(n);
    for (int i = 1; i <= n; i++) {
        map.put(jmvars.get(regExRefName + i + REGEX_GROUP_SUFFIX + grNames), jmvars.get(regExRefName + i + REGEX_GROUP_SUFFIX + grValues));
    }
    return map;
}