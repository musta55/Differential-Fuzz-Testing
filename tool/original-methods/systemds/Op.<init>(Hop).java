public Op(Hop op) {
    _op = op;
    _dim = (int) op.getDim2();
    if (_dim < 0)
        _dim = 16;
}