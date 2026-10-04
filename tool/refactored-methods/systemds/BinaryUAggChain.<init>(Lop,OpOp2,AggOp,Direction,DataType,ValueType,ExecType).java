/**
 * Constructor to setup a map mult chain without weights
 *
 * @param input1 low-level operator
 * @param bop binary operation type
 * @param uaop aggregate operation type
 * @param uadir partial aggregate direction type
 * @param dt data type
 * @param vt value type
 * @param et execution type
 */
public BinaryUAggChain(Lop input1, OpOp2 bop, AggOp uaop, Direction uadir, DataType dt, ValueType vt, ExecType et) {
    super(Lop.Type.BinUaggChain, dt, vt);
    setupInputs(input1);
    setupOperations(bop, uaop, uadir);
    lps.setProperties(inputs, et);
}
// ---- helper method(s) introduced by the refactoring ----
private void setupInputs(Lop input1) {
    //X
    addInput(input1);
    input1.addOutput(this);
}

private void setupOperations(OpOp2 bop, AggOp uaop, Direction uadir) {
    _binOp = bop;
    _uaggOp = uaop;
    _uaggDir = uadir;
}

