public Federated(HashMap<String, Lop> inputLops, DataType dataType, ValueType valueType) {
    super(Type.Federated, dataType, valueType);
    initializeFields(inputLops);
    addInputsAndOutputs();
    handleLocalObject(inputLops);
}
// ---- helper method(s) introduced by the refactoring ----
private void initializeFields(HashMap<String, Lop> inputLops) {
    _type = inputLops.get(FED_TYPE);
    _addresses = inputLops.get(FED_ADDRESSES);
    _ranges = inputLops.get(FED_RANGES);
}

private void addInputsAndOutputs() {
    addInput(_type);
    _type.addOutput(this);
    addInput(_addresses);
    _addresses.addOutput(this);
    addInput(_ranges);
    _ranges.addOutput(this);
}

private void handleLocalObject(HashMap<String, Lop> inputLops) {
    if (inputLops.size() == 4) {
        _localObject = inputLops.get(FED_LOCAL_OBJECT);
        addInput(_localObject);
        _localObject.addOutput(this);
    }
}

