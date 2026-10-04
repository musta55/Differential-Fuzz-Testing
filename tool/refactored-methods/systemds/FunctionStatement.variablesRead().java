@Override
public VariableSet variablesRead() {
    LOG.warn(printWarningLocation() + " -- should not call variablesRead from FunctionStatement");
    return new VariableSet();
}