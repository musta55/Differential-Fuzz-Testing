@Override
public VariableSet variablesUpdated() {
    LOG.warn(printWarningLocation() + " -- should not call variablesUpdated from FunctionStatement");
    return new VariableSet();
}