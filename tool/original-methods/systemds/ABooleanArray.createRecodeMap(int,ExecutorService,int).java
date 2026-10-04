@Override
protected HashMapToInt<Boolean> createRecodeMap(int estimate, ExecutorService pool, int k) {
    HashMapToInt<Boolean> map = new HashMapToInt<Boolean>(2);
    int id = 1;
    for (int i = 0; i < size() && id <= 2; i++) {
        int v = map.putIfAbsentI(get(i), id);
        if (v == -1)
            id++;
    }
    return map;
}