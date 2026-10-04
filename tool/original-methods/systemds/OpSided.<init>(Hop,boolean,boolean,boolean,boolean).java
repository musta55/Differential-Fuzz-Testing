// private final int _dim;
public OpSided(Hop op, boolean cLeft, boolean cRight, boolean tLeft, boolean tRight) {
    super(op);
    _cLeft = cLeft;
    _cRight = cRight;
    _tLeft = tLeft;
    _tRight = tRight;
    _dim = (int) (cLeft ? op.getDim2() : op.getDim1());
    if (_dim < 0)
        _dim = 16;
}