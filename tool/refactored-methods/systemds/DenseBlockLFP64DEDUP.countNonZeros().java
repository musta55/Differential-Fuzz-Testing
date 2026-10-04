@Override
public long countNonZeros() {
    long nnz = 0;
    HashMap<double[], Long> cache = new HashMap<>();
    for (int i = 0; i < _rlen; i++) {
        double[] row = _data[i];
        if (row == null) {
            continue;
        }
        Long count = cache.getOrDefault(row, null);
        if (count == null) {
            count = (long) countNonZeros(i);
            cache.put(row, count);
        }
        nnz += count;
    }
    return nnz;
}