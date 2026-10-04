public Transform(Lop[] inputs, ReOrgOp op, DataType dt, ValueType vt, ExecType et, int k) {
    super(Lop.Type.Transform, dt, vt);
    init(inputs, op, dt, vt, et);
    _numThreads = k;
}