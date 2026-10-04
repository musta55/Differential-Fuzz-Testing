public ArrayList<Pair<String, Set<Integer>>> getAllKeys() {
    List<Key> allKeys = fetchAllKeys();
    List<Key> sortedKeys = sortKeys(allKeys);
    return convertToPairs(sortedKeys);
}
// ---- helper method(s) introduced by the refactoring ----
private List<Key> fetchAllKeys() {
    List<Key> allKeys = new ArrayList<>();
    fetchKeys(root, allKeys, new Key(new StringBuilder(), new ArrayList<>()));
    return allKeys;
}

private void fetchKeys(TextTrieNode node, List<Key> result, Key curKey) {
    if (node.getChildren().isEmpty())
        return;
    for (Character k : node.getChildren().keySet()) {
        TextTrieNode child = node.getChildren().get(k);
        List<Integer> tList = new ArrayList<>(child.getRowIndexes());
        Key key = new Key(new StringBuilder(curKey.getKey()).append(k), tList);
        result.add(key);
        fetchKeys(child, result, key);
    }
}

private List<Key> sortKeys(List<Key> keys) {
    Comparator<Key> compare = Comparator.comparing(Key::getIndexSetSize).thenComparing(Key::getKeyLength).reversed();
    return keys.stream().sorted(compare).collect(Collectors.toList());
}

private ArrayList<Pair<String, Set<Integer>>> convertToPairs(List<Key> keys) {
    ArrayList<Pair<String, Set<Integer>>> result = new ArrayList<>();
    for (Key k : keys) {
        result.add(new Pair<>(k.getKey().toString(), k.getIndexSet()));
    }
    return result;
}

