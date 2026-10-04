public Transform(Lop[] inputs, ReOrgOp op, DataType dt, ValueType vt, ExecType et, boolean bSortIndInMem) {
    this(inputs, op, dt, vt, et, 1, bSortIndInMem);
}
// ---- helper method(s) introduced by the refactoring ----
private Transform(Lop[] inputs, ReOrgOp op, DataType dt, ValueType vt, ExecType et, int k, boolean bSortIndInMem) {
    super(Lop.Type.Transform, dt, vt);
    _bSortIndInMem = bSortIndInMem;
    _numThreads = k;
    init(inputs, op, dt, vt, et);
}

