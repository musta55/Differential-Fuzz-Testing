public void setDimensionValueProperties(Identifier i) {
    if (i instanceof IndexedIdentifier) {
        IndexedIdentifier ixi = (IndexedIdentifier) i;
        _dim1 = ixi.getOrigDim1();
        _dim2 = ixi.getOrigDim2();
    } else {
        _dim1 = i.getDim1();
        _dim2 = i.getDim2();
    }
    _nnz = i.getNnz();
    _dataType = i.getDataType();
    _valueType = i.getValueType();
}