public Transform(Lop input, ReOrgOp op, DataType dt, ValueType vt, ExecType et, boolean bSortIndInMem) {
    super(Lop.Type.Transform, dt, vt);
    _bSortIndInMem = bSortIndInMem;
    init(new Lop[] { input }, op, dt, vt, et);
}