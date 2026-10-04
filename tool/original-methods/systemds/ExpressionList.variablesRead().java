@Override
public VariableSet variablesRead() {
    VariableSet result = new VariableSet();
    for (Expression expr : _value) {
        result.addVariables(expr.variablesRead());
    }
    return result;
}