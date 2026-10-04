@Override
public String toString() {
    StringBuilder sb = new StringBuilder("Function Dictionary:\n----------------------------------------\n");
    int pos = 0;
    for (Entry<String, T> e : _funs.entrySet()) {
        sb.append("-- [").append(pos++).append("]: ").append(e.getKey()).append("\n");
    }
    return sb.toString();
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

