public Binary(Lop input1, Lop input2, OpOp2 op, DataType dt, ValueType vt, ExecType et, int k, boolean inplace) {
    super(Lop.Type.Binary, dt, vt);
    init(input1, input2, op, dt, vt, et);
    _numThreads = k;
    this.inplace = inplace;
}