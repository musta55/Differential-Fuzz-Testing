public Nary(OpOpN operationType, DataType dt, ValueType vt, Lop[] inputLops, ExecType et) {
    super(Lop.Type.Nary, dt, vt);
    this.operationType = operationType;
    for (Lop inputLop : inputLops) {
        addInput(inputLop);
        inputLop.addOutput(this);
    }
    if (et == ExecType.CP || et == ExecType.SPARK) {
        lps.setProperties(inputs, et);
    } else {
        throw new LopsException("Unsupported exec type for nary lop:" + et.name());
    }
}