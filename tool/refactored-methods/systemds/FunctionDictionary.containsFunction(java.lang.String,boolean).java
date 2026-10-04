public boolean containsFunction(String fname, boolean opt) {
    Map<String, T> map = opt ? _funs : _funsOrig;
    return map != null && map.containsKey(fname);
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

