/**
 * Validate parse tree : Process Binary Expression in an assignment
 * statement
 */
@Override
public void validateExpression(HashMap<String, DataIdentifier> ids, HashMap<String, ConstIdentifier> constVars, boolean conditional) {
    //recursive validate
    if (_left instanceof FunctionCallIdentifier || _right instanceof FunctionCallIdentifier) {
        raiseValidateError("User-defined function calls not supported in binary expressions.", false, LanguageException.LanguageErrorCodes.UNSUPPORTED_EXPRESSION);
    }
    _left.validateExpression(ids, constVars, conditional);
    _right.validateExpression(ids, constVars, conditional);
    //constant propagation (precondition for more complex constant folding rewrite)
    if (!conditional) {
        if (_left instanceof DataIdentifier && constVars.containsKey(((DataIdentifier) _left).getName()))
            _left = constVars.get(((DataIdentifier) _left).getName());
        if (_right instanceof DataIdentifier && constVars.containsKey(((DataIdentifier) _right).getName()))
            _right = constVars.get(((DataIdentifier) _right).getName());
    }
    String outputName = getTempName();
    DataIdentifier output = new DataIdentifier(outputName);
    output.setParseInfo(this);
    output.setDataType(computeDataType(this.getLeft(), this.getRight(), true));
    ValueType resultVT = computeValueType(this.getLeft(), this.getRight(), true);
    // Override the computed value type, if needed
    if (this.getOpCode() == Expression.BinaryOp.POW || this.getOpCode() == Expression.BinaryOp.DIV) {
        resultVT = ValueType.FP64;
    }
    output.setValueType(resultVT);
    checkAndSetDimensions(output, conditional);
    if (getOpCode() == Expression.BinaryOp.MATMULT) {
        if ((getLeft().getOutput().getDataType() != DataType.MATRIX) || (getRight().getOutput().getDataType() != DataType.MATRIX)) {
            // remove exception for now
            //		throw new LanguageException(
            //				"Matrix multiplication not supported for scalars",
            //				LanguageException.LanguageErrorCodes.INVALID_PARAMETERS);
        }
        if (getLeft().getOutput().getDim2() != -1 && getRight().getOutput().getDim1() != -1 && getLeft().getOutput().getDim2() != getRight().getOutput().getDim1()) {
            raiseValidateError("invalid dimensions for matrix multiplication (k1=" + getLeft().getOutput().getDim2() + ", k2=" + getRight().getOutput().getDim1() + ")", conditional, LanguageException.LanguageErrorCodes.INVALID_PARAMETERS);
        }
        output.setDimensions(getLeft().getOutput().getDim1(), getRight().getOutput().getDim2());
    }
    this.setOutput(output);
}