public void merge(FunctionDictionary<T> that) {
    //merge optimized functions
    for (Entry<String, T> e : that._funs.entrySet()) if (!_funs.containsKey(e.getKey()))
        _funs.put(e.getKey(), e.getValue());
    //merge unoptimized functions
    if (_funsOrig != null && that._funsOrig != null)
        for (Entry<String, T> e : that._funsOrig.entrySet()) if (!_funsOrig.containsKey(e.getKey()))
            _funsOrig.put(e.getKey(), e.getValue());
}