@Override
public VariableSet variablesRead() {
    VariableSet result = new VariableSet();
    result.addVariables(_fromExpr.variablesRead());
    result.addVariables(_toExpr.variablesRead());
    if (_incrementExpr != null)
        result.addVariables(_incrementExpr.variablesRead());
    return result;
}