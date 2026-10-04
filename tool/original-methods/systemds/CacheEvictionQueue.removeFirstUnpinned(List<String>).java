public Map.Entry<String, ByteBuffer> removeFirstUnpinned(List<String> pinnedList) {
    //move iterator to first entry
    Iterator<Map.Entry<String, ByteBuffer>> iter = entrySet().iterator();
    var entry = iter.next();
    while (pinnedList.contains(entry.getKey())) entry = iter.next();
    //remove current iterator entry
    iter.remove();
    return entry;
}