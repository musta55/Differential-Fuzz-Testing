public void addFunction(String fname, T fsb, boolean opt) {
    Map<String, T> map = opt ? _funs : getOrCreateUnoptimizedFunctions();
    if (map.containsKey(fname))
        throw new DMLRuntimeException("Function '" + fname + "' (" + opt + ") already exists in namespace.");
    map.put(fname, fsb);
}
// ---- helper method(s) introduced by the refactoring ----
private Map<String, T> getOrCreateUnoptimizedFunctions() {
    if (_funsOrig == null) {
        _funsOrig = new HashMap<>();
    }
    return _funsOrig;
}

private void mergeFunctions(Map<String, T> source, Map<String, T> target) {
    if (source != null && target != null) {
        for (Entry<String, T> e : source.entrySet()) if (!target.containsKey(e.getKey()))
            target.put(e.getKey(), e.getValue());
    }
}

