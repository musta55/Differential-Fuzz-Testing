public CumulativeOffsetBinary(Lop data, Lop offsets, DataType dt, ValueType vt, AggOp op, ExecType et) {
    this(data, offsets, dt, vt, 0, false, op, et);
}