private static void updateLiveVariables(VariableSet origVars, VariableSet newVars) {
    newVars.getVariables().entrySet().stream().filter(entry -> origVars.containsVariable(entry.getKey())).forEach(entry -> origVars.addVariable(entry.getKey(), entry.getValue()));
}