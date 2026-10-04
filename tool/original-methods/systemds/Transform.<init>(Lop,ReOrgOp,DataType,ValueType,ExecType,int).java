public Transform(Lop input, ReOrgOp op, DataType dt, ValueType vt, ExecType et, int k) {
    super(Lop.Type.Transform, dt, vt);
    init(new Lop[] { input }, op, dt, vt, et);
    _numThreads = k;
}