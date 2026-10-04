public T getFunction(String fname, boolean opt) {
    //check for existing unoptimized functions if necessary
    if (!opt && _funsOrig == null)
        throw new DMLRuntimeException("Requested unoptimized function " + "'" + fname + "' but original function copies have not been created.");
    //obtain optimized or unoptimized function (null if not available)
    return opt ? _funs.get(fname) : (_funsOrig != null) ? _funsOrig.get(fname) : null;
}