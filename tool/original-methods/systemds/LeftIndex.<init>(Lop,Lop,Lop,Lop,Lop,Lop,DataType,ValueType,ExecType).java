public LeftIndex(Lop lhsInput, Lop rhsInput, Lop rowL, Lop rowU, Lop colL, Lop colU, DataType dt, ValueType vt, ExecType et) {
    super(Lop.Type.LeftIndex, dt, vt);
    _type = LixCacheType.NONE;
    init(lhsInput, rhsInput, rowL, rowU, colL, colU, et);
}