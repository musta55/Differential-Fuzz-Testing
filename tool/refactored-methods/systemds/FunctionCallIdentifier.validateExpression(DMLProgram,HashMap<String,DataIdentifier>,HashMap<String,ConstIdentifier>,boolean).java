/**
 * Validate parse tree : Process ExtBuiltinFunction Expression is an
 * assignment statement
 *
 * NOTE: this does not override the normal validateExpression because it needs to pass dmlp!
 *
 * @param dmlp dml program
 * @param ids map of data identifiers
 * @param constVars map of constant identifiers
 * @param conditional if true, display warning for 'raiseValidateError'; if false, throw LanguageException
 * for 'raiseValidateError'
 */
public void validateExpression(DMLProgram dmlp, HashMap<String, DataIdentifier> ids, HashMap<String, ConstIdentifier> constVars, boolean conditional) {
    checkNamespaceExists(dmlp);
    checkFunctionDefined(dmlp);
    setOpcode();
    checkParameterTypes();
    validateParameterExpressions(ids, constVars, conditional);
    handleBuiltinFunctions(dmlp, conditional);
    validateDefaultParameters(dmlp, ids, constVars, conditional);
    checkNamedParameters(dmlp);
    constantPropagation(constVars);
    checkArgumentCountAndTypes(dmlp);
    setFunctionOutputs(dmlp);
}
// ---- helper method(s) introduced by the refactoring ----
private void checkNamespaceExists(DMLProgram dmlp) {
    if (dmlp.getNamespaces().get(_namespace) == null) {
        raiseValidateError("namespace " + _namespace + " is not defined ", false);
    }
}

private void checkFunctionDefined(DMLProgram dmlp) {
    FunctionStatementBlock fblock = dmlp.getFunctionStatementBlock(_namespace, _name);
    if (fblock == null) {
        // handle private builtin function
        fblock = dmlp.getFunctionStatementBlock(DMLProgram.BUILTIN_NAMESPACE, _name);
        _namespace = DMLProgram.BUILTIN_NAMESPACE;
    }
    if (fblock == null && !Builtins.contains(_name, true, false)) {
        raiseValidateError("function " + _name + " is undefined in namespace " + _namespace, false);
    }
}

private void setOpcode() {
    _opcode = Expression.FunctCallOp.INTERNAL;
}

private void checkParameterTypes() {
    boolean hasNamed = false, hasUnnamed = false;
    for (ParameterExpression paramExpr : _paramExprs) {
        if (paramExpr.getName() == null)
            hasUnnamed = true;
        else
            hasNamed = true;
    }
    if (hasNamed && hasUnnamed) {
        raiseValidateError(" In DML, functions can only have named parameters " + "(e.g., name1=value1, name2=value2) or unnamed parameters (e.g, value1, value2). " + _name + " has both parameter types.", false);
    }
}

private void validateParameterExpressions(HashMap<String, DataIdentifier> ids, HashMap<String, ConstIdentifier> constVars, boolean conditional) {
    for (ParameterExpression paramExpr : _paramExprs) {
        if (paramExpr.getExpr() instanceof FunctionCallIdentifier) {
            raiseValidateError("UDF function call not supported as parameter to function call", false);
        }
        paramExpr.getExpr().validateExpression(ids, constVars, conditional);
    }
}

private void handleBuiltinFunctions(DMLProgram dmlp, boolean conditional) {
    if (Builtins.contains(_name, true, false) && dmlp.getFunctionStatementBlock(_namespace, _name) == null) {
        DataType dt = _paramExprs.get(0).getExpr().getOutput().getDataType();
        _name = Builtins.getInternalFName(_name, dt);
        _namespace = DMLProgram.BUILTIN_NAMESPACE;
        if (dmlp.getFunctionStatementBlock(_namespace, _name) == null) {
            raiseValidateError("Builtin function '" + _name + "': script loaded " + "but function not found. Is there a typo in the function name?", conditional);
        }
    }
}

private void validateDefaultParameters(DMLProgram dmlp, HashMap<String, DataIdentifier> ids, HashMap<String, ConstIdentifier> constVars, boolean conditional) {
    FunctionStatementBlock fblock = dmlp.getFunctionStatementBlock(_namespace, _name);
    FunctionStatement fstmt = (FunctionStatement) fblock.getStatement(0);
    for (Expression expDef : fstmt.getInputDefaults()) {
        if (expDef != null)
            expDef.validateExpression(ids, constVars, conditional);
    }
}

private void checkNamedParameters(DMLProgram dmlp) {
    boolean hasNamed = false;
    for (ParameterExpression paramExpr : _paramExprs) {
        if (paramExpr.getName() != null) {
            hasNamed = true;
            break;
        }
    }
    if (hasNamed) {
        Set<String> params = Arrays.stream(((FunctionStatement) dmlp.getFunctionStatementBlock(_namespace, _name).getStatement(0)).getInputParamNames()).collect(Collectors.toSet());
        for (ParameterExpression paramExpr : _paramExprs) {
            if (!params.contains(paramExpr.getName())) {
                raiseValidateError("Named function call parameter '" + paramExpr.getName() + "'" + " does not exist in signature of function '" + _name + "'. " + "Function signature: " + Arrays.toString(((FunctionStatement) dmlp.getFunctionStatementBlock(_namespace, _name).getStatement(0)).getInputParamNames()), false);
            }
        }
    }
}

private void constantPropagation(HashMap<String, ConstIdentifier> constVars) {
    for (ParameterExpression paramExpr : _paramExprs) {
        Expression expri = paramExpr.getExpr();
        if (expri instanceof DataIdentifier && !(expri instanceof IndexedIdentifier) && constVars.containsKey(((DataIdentifier) expri).getName())) {
            paramExpr.setExpr(constVars.get(((DataIdentifier) expri).getName()));
        }
    }
}

private void checkArgumentCountAndTypes(DMLProgram dmlp) {
    FunctionStatementBlock fblock = dmlp.getFunctionStatementBlock(_namespace, _name);
    FunctionStatement fstmt = (FunctionStatement) fblock.getStatement(0);
    if (fstmt.getInputParams().size() < _paramExprs.size()) {
        raiseValidateError("function " + _name + " has incorrect number of parameters. Function requires " + fstmt.getInputParams().size() + " but was called with " + _paramExprs.size(), false);
    }
}

private void setFunctionOutputs(DMLProgram dmlp) {
    FunctionStatementBlock fblock = dmlp.getFunctionStatementBlock(_namespace, _name);
    FunctionStatement fstmt = (FunctionStatement) fblock.getStatement(0);
    _outputs = new Identifier[fstmt.getOutputParams().size()];
    for (int i = 0; i < fstmt.getOutputParams().size(); i++) {
        _outputs[i] = new DataIdentifier(fstmt.getOutputParams().get(i));
    }
}

