@Override
public VariableSet variablesUpdated() {
    LOG.warn(this.printWarningLocation() + "should not call variablesUpdated from WhileStatement ");
    return new VariableSet();
}