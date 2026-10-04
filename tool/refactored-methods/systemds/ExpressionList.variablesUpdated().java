@Override
public VariableSet variablesUpdated() {
    VariableSet result = new VariableSet();
    for (Expression expr : value) {
        result.addVariables(expr.variablesUpdated());
    }
    return result;
}