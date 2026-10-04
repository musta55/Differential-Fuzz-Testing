public CumulativeOffsetBinary(Lop data, Lop offsets, DataType dt, ValueType vt, AggOp op, ExecType et) {
    super(Lop.Type.CumulativeOffsetBinary, dt, vt);
    checkSupportedOperations(op);
    _op = op;
    init(data, offsets, dt, vt, et);
}