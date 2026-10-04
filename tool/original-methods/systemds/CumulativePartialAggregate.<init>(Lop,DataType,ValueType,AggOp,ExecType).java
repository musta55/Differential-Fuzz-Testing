public CumulativePartialAggregate(Lop input, DataType dt, ValueType vt, AggOp op, ExecType et) {
    super(Lop.Type.CumulativePartialAggregate, dt, vt);
    //sanity check for supported aggregates
    if (!(op == AggOp.SUM || op == AggOp.PROD || op == AggOp.SUM_PROD || op == AggOp.MIN || op == AggOp.MAX)) {
        throw new LopsException("Unsupported aggregate operation type: " + op);
    }
    _op = op;
    init(input, dt, vt, et);
}