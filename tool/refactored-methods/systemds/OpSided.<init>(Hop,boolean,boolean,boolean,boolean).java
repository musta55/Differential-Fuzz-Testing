public OpSided(Hop op, boolean cLeft, boolean cRight, boolean tLeft, boolean tRight) {
    super(op);
    _cLeft = cLeft;
    _cRight = cRight;
    _tLeft = tLeft;
    _tRight = tRight;
    _dim = initializeDim(op, cLeft);
}
// ---- helper method(s) introduced by the refactoring ----
private int initializeDim(Hop op, boolean cLeft) {
    int dim = (int) (cLeft ? op.getDim2() : op.getDim1());
    return dim < 0 ? 16 : dim;
}

