public void setProperties(Identifier i) {
    if (i == null)
        return;
    _dataType = i.getDataType();
    _valueType = i.getValueType();
    if (i instanceof IndexedIdentifier) {
        _dim1 = ((IndexedIdentifier) i).getOrigDim1();
        _dim2 = ((IndexedIdentifier) i).getOrigDim2();
    } else {
        _dim1 = i.getDim1();
        _dim2 = i.getDim2();
    }
    _blocksize = i.getBlocksize();
    _nnz = i.getNnz();
    _format = i.getFileFormat();
}