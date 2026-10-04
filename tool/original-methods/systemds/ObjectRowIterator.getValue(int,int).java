private Object getValue(int i, int j) {
    Object val = _fb.get(i, j);
    if (_tgtSchema != null)
        val = UtilFunctions.objectToObject(_tgtSchema[j], val);
    return val;
}