public TfOffsetMap(List<Pair<Long, Long>> rmRows) {
    TreeMap<Long, Long> sortedMap = sortInputList(rmRows);
    _map = createOffsetTable(sortedMap);
    _rmRows = calculateTotalRemovedRows(sortedMap);
}
// ---- helper method(s) introduced by the refactoring ----
private TreeMap<Long, Long> sortInputList(List<Pair<Long, Long>> rmRows) {
    TreeMap<Long, Long> tmap = new TreeMap<>();
    for (Pair<Long, Long> pair : rmRows) {
        tmap.put(pair.getKey(), pair.getValue());
    }
    return tmap;
}

private HashMap<Long, Long> createOffsetTable(TreeMap<Long, Long> sortedMap) {
    HashMap<Long, Long> map = new HashMap<>();
    long shift = 0;
    for (Entry<Long, Long> e : sortedMap.entrySet()) {
        map.put(e.getKey(), e.getKey() - shift);
        shift += e.getValue();
    }
    return map;
}

private long calculateTotalRemovedRows(TreeMap<Long, Long> sortedMap) {
    long shift = 0;
    for (Entry<Long, Long> e : sortedMap.entrySet()) {
        shift += e.getValue();
    }
    return shift;
}

