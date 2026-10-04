public TfOffsetMap(List<Pair<Long, Long>> rmRows) {
    //sort input list of <key, rm rows per block>
    TreeMap<Long, Long> tmap = new TreeMap<>();
    for (Pair<Long, Long> pair : rmRows) tmap.put(pair.getKey(), pair.getValue());
    //compute shifted keys and build hash table
    _map = new HashMap<>();
    long shift = 0;
    for (Entry<Long, Long> e : tmap.entrySet()) {
        _map.put(e.getKey(), e.getKey() - shift);
        shift += e.getValue();
    }
    _rmRows = shift;
}