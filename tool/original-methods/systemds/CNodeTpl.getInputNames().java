public String[] getInputNames() {
    String[] ret = new String[_inputs.size()];
    for (int i = 0; i < _inputs.size(); i++) ret[i] = _inputs.get(i).getVarname();
    return ret;
}