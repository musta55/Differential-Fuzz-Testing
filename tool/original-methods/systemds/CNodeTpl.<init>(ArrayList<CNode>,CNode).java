public CNodeTpl(ArrayList<CNode> inputs, CNode output) {
    if (inputs.size() < 1)
        throw new RuntimeException("Cannot pass empty inputs to the CNodeTpl");
    for (CNode input : inputs) addInput(input);
    _output = output;
}