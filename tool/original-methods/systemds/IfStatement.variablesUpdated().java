@Override
public VariableSet variablesUpdated() {
    LOG.warn("WARNING: line " + this.getBeginLine() + ", column " + this.getBeginColumn() + " --  should not call variablesUpdated from IfStatement ");
    return null;
}