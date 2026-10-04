/**
 * Constructor to perform a binary operation.
 *
 * @param input1 low-level operator 1
 * @param input2 low-level operator 2
 * @param op operation type
 * @param dt data type
 * @param vt value type
 * @param et exec type
 */
public Binary(Lop input1, Lop input2, OpOp2 op, DataType dt, ValueType vt, ExecType et) {
    this(input1, input2, op, dt, vt, et, 1);
}