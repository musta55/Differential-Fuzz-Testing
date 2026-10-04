public Transform(Lop[] inputs, ReOrgOp op, DataType dt, ValueType vt, ExecType et, boolean bSortIndInMem) {
    super(Lop.Type.Transform, dt, vt);
    _bSortIndInMem = bSortIndInMem;
    init(inputs, op, dt, vt, et);
}