public void put(ColIndexes key, CompressedSizeInfoColGroup val) {
    final IColIndex gi = key._indexes;
    final int bucketID = gi.get(0);
    Map<ColIndexes, CompressedSizeInfoColGroup> bucket = mem[bucketID];
    if (bucket == null)
        bucket = mem[bucketID] = new HashMap<>();
    bucket.put(key, val);
}