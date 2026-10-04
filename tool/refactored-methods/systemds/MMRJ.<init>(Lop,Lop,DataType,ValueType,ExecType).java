/**
 * Constructor to perform a cross product operation.
 *
 * @param input1 low-level operator 1
 * @param input2 low-level operator 2
 * @param dt data type
 * @param vt value type
 * @param et execution type
 */
public MMRJ(Lop input1, Lop input2, DataType dt, ValueType vt, ExecType et) {
    //handle inputs and outputs
    super(Lop.Type.MMRJ, dt, vt);
    initializeInputs(input1, input2);
    setLowLevelProperties(et);
}
// ---- helper method(s) introduced by the refactoring ----
private void initializeInputs(Lop input1, Lop input2) {
    addInput(input1);
    addInput(input2);
    input1.addOutput(this);
    input2.addOutput(this);
}

private void setLowLevelProperties(ExecType et) {
    lps.setProperties(inputs, et);
}

