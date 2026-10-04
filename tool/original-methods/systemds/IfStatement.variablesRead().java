@Override
public VariableSet variablesRead() {
    LOG.warn("WARNING: line " + this.getBeginLine() + ", column " + this.getBeginColumn() + " --  should not call variablesRead from IfStatement ");
    return null;
}