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
    // Step 1: check the namespace exists, and that function is defined in the namespace
    if (dmlp.getNamespaces().get(_namespace) == null) {
        raiseValidateError("namespace " + _namespace + " is not defined ", conditional);
    }
    FunctionStatementBlock fblock = dmlp.getFunctionStatementBlock(_namespace, _name);
    if (fblock == null) {
        //handle private builtin function
        fblock = dmlp.getFunctionStatementBlock(DMLProgram.BUILTIN_NAMESPACE, _name);
        _namespace = DMLProgram.BUILTIN_NAMESPACE;
    }
    if (fblock == null && !Builtins.contains(_name, true, false)) {
        raiseValidateError("function " + _name + " is undefined in namespace " + _namespace, conditional);
    }
    // Step 2: set opcode (whether internal or external function) -- based on whether FunctionStatement
    // in FunctionStatementBlock is ExternalFunctionStatement or FunctionStatement
    _opcode = Expression.FunctCallOp.INTERNAL;
    // Step 3: check all parameters to be either unnamed or named for functions
    boolean hasNamed = false, hasUnnamed = false;
    for (ParameterExpression paramExpr : _paramExprs) {
        if (paramExpr.getName() == null)
            hasUnnamed = true;
        else
            hasNamed = true;
    }
    if (hasNamed && hasUnnamed) {
        raiseValidateError(" In DML, functions can only have named parameters " + "(e.g., name1=value1, name2=value2) or unnamed parameters (e.g, value1, value2). " + _name + " has both parameter types.", conditional);
    }
    // Step 4: validate expressions for each passed parameter
    for (ParameterExpression paramExpr : _paramExprs) {
        if (paramExpr.getExpr() instanceof FunctionCallIdentifier) {
            raiseValidateError("UDF function call not supported as parameter to function call", false);
        }
        paramExpr.getExpr().validateExpression(ids, constVars, conditional);
    }
    // Step 5: replace dml-bodied builtin function calls after type inference
    if (Builtins.contains(_name, true, false) && fblock == null) {
        DataType dt = _paramExprs.get(0).getExpr().getOutput().getDataType();
        _name = Builtins.getInternalFName(_name, dt);
        _namespace = DMLProgram.BUILTIN_NAMESPACE;
        fblock = dmlp.getFunctionStatementBlock(_namespace, _name);
        if (fblock == null) {
            raiseValidateError("Builtin function '" + _name + "': script loaded " + "but function not found. Is there a typo in the function name?", conditional);
            //robustness on warnings (conditional)
            return;
        }
    }
    // Step 6: validate default parameters (after block assignment)
    FunctionStatement fstmt = (FunctionStatement) fblock.getStatement(0);
    for (Expression expDef : fstmt.getInputDefaults()) {
        if (expDef != null)
            expDef.validateExpression(ids, constVars, conditional);
    }
    // check existence/correctness of named parameters
    if (hasNamed) {
        Set<String> params = Arrays.stream(fstmt.getInputParamNames()).collect(Collectors.toSet());
        for (ParameterExpression paramExpr : _paramExprs) if (!params.contains(paramExpr.getName()))
            raiseValidateError("Named function call parameter '" + paramExpr.getName() + "'" + " does not exist in signature of function '" + fstmt.getName() + "'. " + "Function signature: " + Arrays.toString(fstmt.getInputParamNames()));
    }
    // Step 7: constant propagation into function call statement
    if (!conditional) {
        for (ParameterExpression paramExpr : _paramExprs) {
            Expression expri = paramExpr.getExpr();
            if (expri instanceof DataIdentifier && !(expri instanceof IndexedIdentifier) && constVars.containsKey(((DataIdentifier) expri).getName())) {
                //replace varname with constant in function call expression
                paramExpr.setExpr(constVars.get(((DataIdentifier) expri).getName()));
            }
        }
    }
    // Step 8: check correctness of number of arguments and their types
    if (fstmt.getInputParams().size() < _paramExprs.size()) {
        raiseValidateError("function " + _name + " has incorrect number of parameters. Function requires " + fstmt.getInputParams().size() + " but was called with " + _paramExprs.size(), conditional);
    }
    // Step 9: set the outputs for the function
    _outputs = new Identifier[fstmt.getOutputParams().size()];
    for (int i = 0; i < fstmt.getOutputParams().size(); i++) {
        _outputs[i] = new DataIdentifier(fstmt.getOutputParams().get(i));
    }
}