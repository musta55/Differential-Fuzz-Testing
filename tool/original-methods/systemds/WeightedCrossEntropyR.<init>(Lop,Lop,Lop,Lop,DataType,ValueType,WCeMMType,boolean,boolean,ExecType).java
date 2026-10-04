public WeightedCrossEntropyR(Lop input1, Lop input2, Lop input3, Lop input4, DataType dt, ValueType vt, WCeMMType wt, boolean cacheU, boolean cacheV, ExecType et) {
    super(Lop.Type.WeightedCeMM, dt, vt);
    //X
    addInput(input1);
    //U
    addInput(input2);
    //V
    addInput(input3);
    //optional
    addInput(input4);
    input1.addOutput(this);
    input2.addOutput(this);
    input3.addOutput(this);
    input4.addOutput(this);
    //setup mapmult parameters
    _wcemmType = wt;
    _cacheU = cacheU;
    _cacheV = cacheV;
    setupLopProperties(et);
}