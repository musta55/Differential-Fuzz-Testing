@Override
protected HashMapToInt<Boolean> createRecodeMap(int estimate, ExecutorService pool, int k) {
    HashMapToInt<Boolean> map = new HashMapToInt<>(estimate);
    populateRecodeMap(map);
    return map;
}
// ---- helper method(s) introduced by the refactoring ----
private void populateRecodeMap(HashMapToInt<Boolean> map) {
    int id = 1;
    for (int i = 0; i < size() && id <= 2; i++) {
        int v = map.putIfAbsentI(get(i), id);
        if (v == -1)
            id++;
    }
}

