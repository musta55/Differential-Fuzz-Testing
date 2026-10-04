/**
 * Constructor to perform a cross product operation.
 *
 * @param input1 low-level operator 1
 * @param input2 low-level operator 2
 * @param dt data type
 * @param vt value type
 * @param type cross production operation type (aggregate or no aggregate)
 * @param et execution type
 */
public MMCJ(Lop input1, Lop input2, DataType dt, ValueType vt, MMCJType type, ExecType et) {
    super(Lop.Type.MMCJ, dt, vt);
    addInput(input1);
    addInput(input2);
    input1.addOutput(this);
    input2.addOutput(this);
    _type = type;
    lps.setProperties(inputs, et);
}