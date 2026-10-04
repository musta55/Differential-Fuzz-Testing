public Federated(HashMap<String, Lop> inputLops, DataType dataType, ValueType valueType) {
    super(Type.Federated, dataType, valueType);
    _type = inputLops.get(FED_TYPE);
    _addresses = inputLops.get(FED_ADDRESSES);
    _ranges = inputLops.get(FED_RANGES);
    addInput(_type);
    _type.addOutput(this);
    addInput(_addresses);
    _addresses.addOutput(this);
    addInput(_ranges);
    _ranges.addOutput(this);
    if (inputLops.size() == 4) {
        _localObject = inputLops.get(FED_LOCAL_OBJECT);
        addInput(_localObject);
        _localObject.addOutput(this);
    }
}