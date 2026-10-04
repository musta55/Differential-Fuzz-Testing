@Override
public VariableSet variablesUpdated() {
    LOG.warn(this.printWarningLocation() + "should not call variablesRead from WhileStatement ");
    return new VariableSet();
}