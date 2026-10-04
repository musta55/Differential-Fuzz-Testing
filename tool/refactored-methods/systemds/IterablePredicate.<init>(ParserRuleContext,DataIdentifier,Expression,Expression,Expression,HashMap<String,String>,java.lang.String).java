public IterablePredicate(ParserRuleContext ctx, DataIdentifier iterVar, Expression fromExpr, Expression toExpr, Expression incrementExpr, HashMap<String, String> parForParamValues, String filename) {
    initializeFields(iterVar, fromExpr, toExpr, incrementExpr, parForParamValues);
    setCtxValuesAndFilename(ctx, filename);
}
// ---- helper method(s) introduced by the refactoring ----
private void initializeFields(DataIdentifier iterVar, Expression fromExpr, Expression toExpr, Expression incrementExpr, HashMap<String, String> parForParamValues) {
    _iterVar = iterVar;
    _fromExpr = fromExpr;
    _toExpr = toExpr;
    _incrementExpr = incrementExpr;
    _parforParams = parForParamValues;
}

private void addVariablesFromExpression(VariableSet result, Expression expr) {
    result.addVariables(expr.variablesRead());
}

private void addVariablesFromExpressionIfNotNull(VariableSet result, Expression expr) {
    if (expr != null) {
        result.addVariables(expr.variablesRead());
    }
}

private void validateFunctionCalls(HashMap<String, DataIdentifier> ids, HashMap<String, ConstIdentifier> constVars) {
    if (_iterVar instanceof FunctionCallIdentifier || _fromExpr instanceof FunctionCallIdentifier || _toExpr instanceof FunctionCallIdentifier || _incrementExpr instanceof FunctionCallIdentifier) {
        raiseValidateError("user-defined function calls not supported for iterable predicates", false, LanguageException.LanguageErrorCodes.UNSUPPORTED_EXPRESSION);
    }
}

private void validateIterationVariable(HashMap<String, DataIdentifier> ids) {
    if (ids.containsKey(_iterVar.getName())) {
        DataIdentifier otherDI = ids.get(_iterVar.getName());
        if (otherDI.getDataType() != DataType.SCALAR || otherDI.getValueType() != ValueType.INT64) {
            raiseValidateError("iterable predicate in for loop '" + _iterVar.getName() + "' must be a scalar integer", false);
        }
    }
    _iterVar.setIntProperties();
    ids.put(_iterVar.getName(), _iterVar);
}

private void validateForPredicate(HashMap<String, DataIdentifier> ids, HashMap<String, ConstIdentifier> constVars, boolean conditional) {
    handleDefaultIncrement();
    validateExpressions(ids, constVars, conditional);
    checkNumericScalarOutputs();
}

private void handleDefaultIncrement() {
    if (_incrementExpr == null && _fromExpr instanceof ConstIdentifier && _toExpr instanceof ConstIdentifier) {
        ConstIdentifier cFrom = (ConstIdentifier) _fromExpr;
        ConstIdentifier cTo = (ConstIdentifier) _toExpr;
        _incrementExpr = new IntIdentifier((cFrom.getLongValue() <= cTo.getLongValue()) ? 1 : -1, this);
    }
}

private void validateExpressions(HashMap<String, DataIdentifier> ids, HashMap<String, ConstIdentifier> constVars, boolean conditional) {
    _fromExpr.validateExpression(ids, constVars, conditional);
    _toExpr.validateExpression(ids, constVars, conditional);
    if (_incrementExpr != null) {
        _incrementExpr.validateExpression(ids, constVars, conditional);
    }
}

private void checkNumericScalarOutputs() {
    checkNumericScalarOutput(_fromExpr);
    checkNumericScalarOutput(_toExpr);
    checkNumericScalarOutput(_incrementExpr);
}

private void appendIncrementExpression(StringBuilder sb) {
    if (_incrementExpr != null) {
        sb.append(",");
        sb.append(_incrementExpr.toString());
    }
}

private void appendParForParams(StringBuilder sb) {
    if (_parforParams != null && !_parforParams.isEmpty()) {
        for (String key : _parforParams.keySet()) {
            sb.append(",");
            sb.append(key);
            sb.append("=");
            sb.append(_parforParams.get(key));
        }
    }
}

