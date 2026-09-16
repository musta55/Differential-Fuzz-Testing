@Override
public Set<String> diffConfig(Map<String, String> oldValues, Map<String, String> newValues) {
    oldValues = handleNullMap(oldValues);
    newValues = handleNullMap(newValues);
    Set<String> changedAttribs = new HashSet<>();
    Set<String> oldKeys = new HashSet<>(oldValues.keySet());
    for (Map.Entry<String, String> newPropEntry : newValues.entrySet()) {
        String key = newPropEntry.getKey();
        if (oldValues.containsKey(key)) {
            if (compare(oldValues.get(key), newPropEntry.getValue()) != 0) {
                changedAttribs.add(key);
            }
            oldKeys.remove(key);
        } else {
            changedAttribs.add(key);
        }
    }
    changedAttribs.addAll(oldKeys);
    return changedAttribs;
}
// ---- helper method(s) introduced by the refactoring ----
private Map<String, String> handleNullMap(Map<String, String> map) {
    return map == null ? Collections.emptyMap() : map;
}

