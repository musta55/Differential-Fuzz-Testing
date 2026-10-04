/**
 * Validate parse tree : Process Boolean Expression
 */
@Override
public void validateExpression(HashMap<String, DataIdentifier> ids, HashMap<String, ConstIdentifier> constVars, boolean conditional) {
    //recursive validate
    getLeft().validateExpression(ids, constVars, conditional);
    if (_left instanceof FunctionCallIdentifier) {
        raiseValidateError("user-defined function calls not supported in boolean expressions", false, LanguageException.LanguageErrorCodes.UNSUPPORTED_EXPRESSION);
    }
    if (this.getRight() != null) {
        if (_right instanceof FunctionCallIdentifier) {
            raiseValidateError("user-defined function calls not supported in boolean expressions", false, LanguageException.LanguageErrorCodes.UNSUPPORTED_EXPRESSION);
        }
        this.getRight().validateExpression(ids, constVars, conditional);
    }
    String outputName = getTempName();
    DataIdentifier output = new DataIdentifier(outputName);
    output.setParseInfo(this);
    if (getLeft().getOutput().getDataType().isMatrix() || (getRight() != null && getRight().getOutput().getDataType().isMatrix())) {
        output.setDataType((getRight() == null) ? DataType.MATRIX : computeDataType(this.getLeft(), this.getRight(), true));
        //since SystemDS only supports double matrices, the value type is forced to
        //double; once we support boolean matrices this needs to change
        output.setValueType(ValueType.FP64);
    } else {
        output.setBooleanProperties();
    }
    this.setOutput(output);
    if ((_opcode == Expression.BooleanOp.CONDITIONALAND) || (_opcode == Expression.BooleanOp.CONDITIONALOR)) {
        // always unconditional (because unsupported operation)
        if (_opcode == Expression.BooleanOp.CONDITIONALAND) {
            raiseValidateError("Conditional AND (&&) not supported.", false);
        } else if (_opcode == Expression.BooleanOp.CONDITIONALOR) {
            raiseValidateError("Conditional OR (||) not supported.", false);
        }
    }
}