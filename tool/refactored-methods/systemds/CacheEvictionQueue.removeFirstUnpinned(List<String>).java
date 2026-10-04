public Map.Entry<String, ByteBuffer> removeFirstUnpinned(List<String> pinnedFileNames) {
    //move iterator to first entry
    Iterator<Map.Entry<String, ByteBuffer>> iter = entrySet().iterator();
    var entry = iter.next();
    while (pinnedFileNames.contains(entry.getKey())) entry = iter.next();
    //remove current iterator entry
    iter.remove();
    return entry;
}