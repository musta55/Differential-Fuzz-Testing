private void checkAndSetDimensions(DataIdentifier output, boolean conditional) {
    Identifier left = this.getLeft().getOutput();
    Identifier right = this.getRight().getOutput();
    Identifier pivot = null;
    Identifier aux = null;
    if (left.getDataType() == DataType.MATRIX) {
        pivot = left;
        if (right.getDataType() == DataType.MATRIX) {
            aux = right;
        }
    } else if (right.getDataType() == DataType.MATRIX) {
        pivot = right;
    }
    if ((pivot != null) && (aux != null)) {
        checkBinaryOperationDimensions(pivot, aux, conditional);
    }
    if (pivot != null) {
        output.setDimensions(pivot.getDim1(), pivot.getDim2());
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void validateFunctionCalls() {
    if (_left instanceof FunctionCallIdentifier || _right instanceof FunctionCallIdentifier) {
        raiseValidateError("User-defined function calls not supported in binary expressions.", false, LanguageException.LanguageErrorCodes.UNSUPPORTED_EXPRESSION);
    }
}

private void validateChildren(HashMap<String, DataIdentifier> ids, HashMap<String, ConstIdentifier> constVars, boolean conditional) {
    _left.validateExpression(ids, constVars, conditional);
    _right.validateExpression(ids, constVars, conditional);
}

private void performConstantPropagation(HashMap<String, ConstIdentifier> constVars, boolean conditional) {
    if (!conditional) {
        replaceWithConstantIfPossible(_left, constVars);
        replaceWithConstantIfPossible(_right, constVars);
    }
}

private void replaceWithConstantIfPossible(Expression expr, HashMap<String, ConstIdentifier> constVars) {
    if (expr instanceof DataIdentifier && constVars.containsKey(((DataIdentifier) expr).getName())) {
        expr = constVars.get(((DataIdentifier) expr).getName());
    }
}

private DataIdentifier createOutputIdentifier() {
    String outputName = getTempName();
    DataIdentifier output = new DataIdentifier(outputName);
    output.setParseInfo(this);
    return output;
}

private void setOutputDataTypeAndValueType(DataIdentifier output) {
    output.setDataType(computeDataType(this.getLeft(), this.getRight(), true));
    ValueType resultVT = computeValueType(this.getLeft(), this.getRight(), true);
    overrideValueTypeForSpecificOperations(resultVT);
    output.setValueType(resultVT);
}

private void overrideValueTypeForSpecificOperations(ValueType resultVT) {
    if (this.getOpCode() == Expression.BinaryOp.POW || this.getOpCode() == Expression.BinaryOp.DIV) {
        resultVT = ValueType.FP64;
    }
}

private void handleMatrixMultiplication(DataIdentifier output, boolean conditional) {
    if (getOpCode() == Expression.BinaryOp.MATMULT) {
        validateMatrixMultiplicationOperands();
        validateMatrixMultiplicationDimensions(conditional);
        setMatrixMultiplicationOutputDimensions(output);
    }
}

private void validateMatrixMultiplicationOperands() {
    if ((getLeft().getOutput().getDataType() != DataType.MATRIX) || (getRight().getOutput().getDataType() != DataType.MATRIX)) {
        // remove exception for now
        // throw new LanguageException(
        // "Matrix multiplication not supported for scalars",
        // LanguageException.LanguageErrorCodes.INVALID_PARAMETERS);
    }
}

private void validateMatrixMultiplicationDimensions(boolean conditional) {
    if (getLeft().getOutput().getDim2() != -1 && getRight().getOutput().getDim1() != -1 && getLeft().getOutput().getDim2() != getRight().getOutput().getDim1()) {
        raiseValidateError("invalid dimensions for matrix multiplication (k1=" + getLeft().getOutput().getDim2() + ", k2=" + getRight().getOutput().getDim1() + ")", conditional, LanguageException.LanguageErrorCodes.INVALID_PARAMETERS);
    }
}

private void setMatrixMultiplicationOutputDimensions(DataIdentifier output) {
    output.setDimensions(getLeft().getOutput().getDim1(), getRight().getOutput().getDim2());
}

private void checkBinaryOperationDimensions(Identifier pivot, Identifier aux, boolean conditional) {
    if (isSameDimensionBinaryOp(this.getOpCode()) && pivot.dimsKnown() && aux.dimsKnown()) {
        if ((pivot.getDim1() != aux.getDim1() && aux.getDim1() > 1) || (pivot.getDim2() != aux.getDim2() && aux.getDim2() > 1)) {
            raiseValidateError("Mismatch in dimensions for operation '" + this.getText() + "'. " + pivot + " is " + pivot.getDim1() + "x" + pivot.getDim2() + " and " + aux + " is " + aux.getDim1() + "x" + aux.getDim2() + ".", conditional);
        }
    }
}

