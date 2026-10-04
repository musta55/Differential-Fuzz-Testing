public Op(Hop hopOperation) {
    this.hopOperation = hopOperation;
    _dim = (int) hopOperation.getDim2();
    if (_dim < 0)
        _dim = 16;
}