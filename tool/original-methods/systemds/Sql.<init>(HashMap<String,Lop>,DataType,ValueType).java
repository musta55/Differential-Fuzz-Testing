public Sql(HashMap<String, Lop> inputParametersLops, DataType dt, ValueType vt) {
    super(Lop.Type.Sql, dt, vt);
    _inputParams = inputParametersLops;
    Lop lop = inputParametersLops.get(SQL_CONN);
    addInput(lop);
    lop.addOutput(this);
    lop = inputParametersLops.get(SQL_USER);
    addInput(lop);
    lop.addOutput(this);
    lop = inputParametersLops.get(SQL_PASS);
    addInput(lop);
    lop.addOutput(this);
    lop = inputParametersLops.get(SQL_QUERY);
    addInput(lop);
    lop.addOutput(this);
}