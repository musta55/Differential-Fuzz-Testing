private void checkNumericScalarOutput(Expression expr) {
    if (expr == null || expr.getOutput() == null)
        return;
    Identifier ident = expr.getOutput();
    if (ident.getDataType() == DataType.MATRIX || (ident.getDataType() == DataType.SCALAR && (ident.getValueType() == ValueType.BOOLEAN || ident.getValueType() == ValueType.STRING))) {
        throw new LanguageException(this.printErrorLocation() + "expression in iterable predicate in for loop '" + expr.toString() + "' must return a numeric scalar");
    }
}