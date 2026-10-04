@Override
public VariableSet variablesRead() {
    VariableSet variableSet = new VariableSet();
    for (Expression expression : expressions) {
        VariableSet variablesRead = expression.variablesRead();
        variableSet.addVariables(variablesRead);
    }
    return variableSet;
}