public boolean containsFunction(String fname, boolean opt) {
    return opt ? _funs.containsKey(fname) : (_funsOrig != null && _funsOrig.containsKey(fname));
}