public void addFunction(String fname, T fsb) {
    if (_funs.containsKey(fname))
        throw new DMLRuntimeException("Function '" + fname + "' already existing in namespace.");
    //add function to existing maps
    _funs.put(fname, fsb);
    if (_funsOrig != null)
        _funsOrig.put(fname, fsb);
}