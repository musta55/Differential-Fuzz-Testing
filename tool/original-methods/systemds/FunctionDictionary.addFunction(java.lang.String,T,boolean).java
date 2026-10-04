public void addFunction(String fname, T fsb, boolean opt) {
    if (!opt && _funsOrig == null)
        _funsOrig = new HashMap<>();
    Map<String, T> map = opt ? _funs : _funsOrig;
    if (map.containsKey(fname))
        throw new DMLRuntimeException("Function '" + fname + "' (" + opt + ") already existing in namespace.");
    map.put(fname, fsb);
}