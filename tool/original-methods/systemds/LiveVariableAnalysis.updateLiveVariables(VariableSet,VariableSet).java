private static void updateLiveVariables(VariableSet origVars, VariableSet newVars) {
    for (String var : newVars.getVariables().keySet()) {
        if (origVars.containsVariable(var)) {
            DataIdentifier varId = newVars.getVariable(var);
            if (varId != null) {
                origVars.addVariable(var, varId);
            }
        }
    }
}