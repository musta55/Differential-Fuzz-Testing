public Expression getInputDefault(String name) {
    for (int i = 0; i < _inputParams.size(); i++) if (_inputParams.get(i).getName().equals(name))
        return _inputDefaults.get(i);
    return null;
}