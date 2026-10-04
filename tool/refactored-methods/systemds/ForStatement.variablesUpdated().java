@Override
public VariableSet variablesUpdated() {
    LOG.error(this.printErrorLocation() + "should not call variablesUpdated from ForStatement ");
    return new VariableSet();
}