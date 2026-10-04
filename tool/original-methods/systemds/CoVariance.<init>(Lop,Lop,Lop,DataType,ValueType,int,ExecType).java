public CoVariance(Lop input1, Lop input2, Lop input3, DataType dt, ValueType vt, int numThreads, ExecType et) {
    super(Lop.Type.CoVariance, dt, vt);
    init(input1, input2, input3, et);
    _numThreads = numThreads;
}