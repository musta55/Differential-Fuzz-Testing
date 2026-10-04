public String[] getInputParamNames() {
    return _inputParams.stream().map(DataIdentifier::getName).toArray(String[]::new);
}