public String[] getInputParamNames() {
    return _inputParams.stream().map(d -> d.getName()).toArray(String[]::new);
}