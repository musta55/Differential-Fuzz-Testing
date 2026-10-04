@Override
public void validateExpression(HashMap<String, DataIdentifier> ids, HashMap<String, ConstIdentifier> constVars, boolean conditional) {
    //recursive validate
    if (_iterVar instanceof FunctionCallIdentifier || _fromExpr instanceof FunctionCallIdentifier || _toExpr instanceof FunctionCallIdentifier || _incrementExpr instanceof FunctionCallIdentifier) {
        raiseValidateError("user-defined function calls not supported for iterable predicates", false, LanguageException.LanguageErrorCodes.UNSUPPORTED_EXPRESSION);
    }
    //1) VALIDATE ITERATION VARIABLE (index)
    // check the variable has either 1) not been defined already OR 2) defined as integer scalar
    if (ids.containsKey(_iterVar.getName())) {
        DataIdentifier otherDI = ids.get(_iterVar.getName());
        if (otherDI.getDataType() != DataType.SCALAR || otherDI.getValueType() != ValueType.INT64) {
            raiseValidateError("iterable predicate in for loop '" + _iterVar.getName() + "' must be a scalar integer", conditional);
        }
    }
    // set the values for DataIdentifer iterable variable
    _iterVar.setIntProperties();
    // add the iterVar to the variable set
    ids.put(_iterVar.getName(), _iterVar);
    //2) VALIDATE FOR PREDICATE in (from, to, increment)
    // handle default increment if unspecified
    if (_incrementExpr == null && _fromExpr instanceof ConstIdentifier && _toExpr instanceof ConstIdentifier) {
        ConstIdentifier cFrom = (ConstIdentifier) _fromExpr;
        ConstIdentifier cTo = (ConstIdentifier) _toExpr;
        _incrementExpr = new IntIdentifier((cFrom.getLongValue() <= cTo.getLongValue()) ? 1 : -1, this);
    }
    //recursively validate the individual expression
    _fromExpr.validateExpression(ids, constVars, conditional);
    _toExpr.validateExpression(ids, constVars, conditional);
    if (_incrementExpr != null)
        _incrementExpr.validateExpression(ids, constVars, conditional);
    //check for scalar expression output
    checkNumericScalarOutput(_fromExpr);
    checkNumericScalarOutput(_toExpr);
    checkNumericScalarOutput(_incrementExpr);
}