public void setDimensionValueProperties(Identifier i) {
    if (i == null)
        return;
    copyDimensionValues(i);
    _dataType = i.getDataType();
    _valueType = i.getValueType();
}
// ---- helper method(s) introduced by the refactoring ----
private void copyProperties(Identifier i) {
    _dataType = i.getDataType();
    _valueType = i.getValueType();
    copyDimensions(i);
    _blocksize = i.getBlocksize();
    _nnz = i.getNnz();
    _format = i.getFileFormat();
}

private void copyDimensionValues(Identifier i) {
    copyDimensions(i);
    _nnz = i.getNnz();
}

private void copyDimensions(Identifier i) {
    if (i instanceof IndexedIdentifier) {
        IndexedIdentifier ixi = (IndexedIdentifier) i;
        _dim1 = ixi.getOrigDim1();
        _dim2 = ixi.getOrigDim2();
    } else {
        _dim1 = i.getDim1();
        _dim2 = i.getDim2();
    }
}

private void validateIndexedIdentifier(IndexedIdentifier ixId, HashMap<String, DataIdentifier> ids, HashMap<String, ConstIdentifier> constVars, boolean conditional) {
    validateIndexBounds(ixId, ids, constVars, conditional);
    updateDimensions(ixId, ids, constVars, conditional);
}

private void validateIndexBounds(IndexedIdentifier ixId, HashMap<String, DataIdentifier> ids, HashMap<String, ConstIdentifier> constVars, boolean conditional) {
    Expression[] exp = new Expression[] { ixId.getRowLowerBound(), ixId.getRowUpperBound(), ixId.getColLowerBound(), ixId.getColUpperBound() };
    String[] msg = new String[] { "row lower", "row upper", "column lower", "column upper" };
    for (int i = 0; i < 4; i++) {
        if (exp[i] != null) {
            exp[i].validateExpression(ids, constVars, conditional);
            if (exp[i].getOutput().getDataType() == DataType.MATRIX) {
                raiseValidateError("Matrix values for " + msg[i] + " index bound are " + "not supported, which includes indexed identifiers.", conditional);
            }
        }
    }
}

private void updateDimensions(IndexedIdentifier ixId, HashMap<String, DataIdentifier> ids, HashMap<String, ConstIdentifier> constVars, boolean conditional) {
    if (getOutput().getDataType() == DataType.LIST) {
        int dim1 = (ixId.getRowUpperBound() == null) ? 1 : -1;
        ixId.setDimensions(dim1, 1);
    } else {
        //default
        IndexPair updatedIndices = ixId.calculateIndexedDimensions(ids, constVars, conditional);
        ixId.setDimensions(updatedIndices._row, updatedIndices._col);
    }
}

