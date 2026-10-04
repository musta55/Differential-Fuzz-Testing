public TernaryAggregate(Lop input1, Lop input2, Lop input3, AggOp aggOp, OpOp2 binOp, Direction direction, DataType dt, ValueType vt, ExecType et, int k) {
    super(Lop.Type.TernaryAggregate, dt, vt);
    //_aggOp = aggOp;
    //_binOp = binOp;
    addInput(input1);
    addInput(input2);
    addInput(input3);
    input1.addOutput(this);
    input2.addOutput(this);
    input3.addOutput(this);
    _direction = direction;
    _numThreads = k;
    lps.setProperties(inputs, et);
}