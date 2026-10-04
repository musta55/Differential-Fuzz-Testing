public WeightedSigmoidR(Lop input1, Lop input2, Lop input3, DataType dt, ValueType vt, WSigmoidType wt, boolean cacheU, boolean cacheV, ExecType et) {
    super(Lop.Type.WeightedSigmoid, dt, vt);
    //X
    addInput(input1);
    //U
    addInput(input2);
    //V
    addInput(input3);
    input1.addOutput(this);
    input2.addOutput(this);
    input3.addOutput(this);
    //setup mapmult parameters
    _wsType = wt;
    _cacheU = cacheU;
    _cacheV = cacheV;
    setupLopProperties(et);
}