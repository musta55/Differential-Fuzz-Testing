public Transform(Lop[] inputs, ReOrgOp op, DataType dt, ValueType vt, boolean outputEmptyBlock, ExecType et) {
    this(inputs, op, dt, vt, et, 1);
    _outputEmptyBlock = outputEmptyBlock;
}