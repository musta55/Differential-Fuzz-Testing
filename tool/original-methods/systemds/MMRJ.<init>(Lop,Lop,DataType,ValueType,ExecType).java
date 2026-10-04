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
    addInput(input1);
    addInput(input2);
    input1.addOutput(this);
    input2.addOutput(this);
    lps.setProperties(inputs, et);
}