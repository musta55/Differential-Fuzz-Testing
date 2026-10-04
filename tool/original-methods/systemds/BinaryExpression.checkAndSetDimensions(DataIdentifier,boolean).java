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
        // check dimensions binary operations (if dims known)
        if (isSameDimensionBinaryOp(this.getOpCode()) && pivot.dimsKnown() && aux.dimsKnown()) {
            // number of rows must always be equivalent if not row vector
            // number of cols must be equivalent if not col vector
            if ((pivot.getDim1() != aux.getDim1() && aux.getDim1() > 1) || (pivot.getDim2() != aux.getDim2() && aux.getDim2() > 1)) {
                raiseValidateError("Mismatch in dimensions for operation '" + this.getText() + "'. " + pivot + " is " + pivot.getDim1() + "x" + pivot.getDim2() + " and " + aux + " is " + aux.getDim1() + "x" + aux.getDim2() + ".", conditional);
            }
        }
    }
    //set dimension information
    if (pivot != null) {
        output.setDimensions(pivot.getDim1(), pivot.getDim2());
    }
}