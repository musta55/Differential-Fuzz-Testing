public WeightedCrossEntropyR(Lop input1, Lop input2, Lop input3, Lop input4, DataType dt, ValueType vt, WCeMMType wt, boolean cacheU, boolean cacheV, ExecType et) {
    this(new Config(input1, input2, input3, input4, dt, vt, wt, cacheU, cacheV, et));
}
// ---- helper method(s) introduced by the refactoring ----
private WeightedCrossEntropyR(Config config) {
    super(Lop.Type.WeightedCeMM, config.dt, config.vt);
    //X
    addInput(config.input1);
    //U
    addInput(config.input2);
    //V
    addInput(config.input3);
    //optional
    addInput(config.input4);
    config.input1.addOutput(this);
    config.input2.addOutput(this);
    config.input3.addOutput(this);
    config.input4.addOutput(this);
    //setup mapmult parameters
    _wcemmType = config.wt;
    _cacheU = config.cacheU;
    _cacheV = config.cacheV;
    setupLopProperties(config.et);
}

