@Override
public VariableSet variablesUpdated() {
    LOG.error(this.printErrorLocation() + "should not call variablesRead from ForStatement ");
    return new VariableSet();
}