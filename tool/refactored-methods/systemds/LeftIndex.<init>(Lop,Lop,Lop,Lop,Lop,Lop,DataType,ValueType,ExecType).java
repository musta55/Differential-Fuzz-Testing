public LeftIndex(Lop lhsInput, Lop rhsInput, Lop rowL, Lop rowU, Lop colL, Lop colU, DataType dt, ValueType vt, ExecType et) {
    this(lhsInput, rhsInput, rowL, rowU, colL, colU, dt, vt, et, LixCacheType.NONE);
}