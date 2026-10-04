public void put(ColIndexes key, CompressedSizeInfoColGroup val) {
    int bucketID = key._indexes.get(0);
    Map<ColIndexes, CompressedSizeInfoColGroup> bucket = mem[bucketID];
    if (bucket == null) {
        bucket = new HashMap<>();
        mem[bucketID] = bucket;
    }
    bucket.put(key, val);
}