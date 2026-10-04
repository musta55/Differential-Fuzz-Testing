public ArrayList<Pair<String, Set<Integer>>> getAllKeys() {
    ArrayList<Pair<String, Set<Integer>>> result = new ArrayList<>();
    ArrayList<Key> allKeys = new ArrayList<>();
    getAllKeys(root, allKeys, new Key(new StringBuilder(), new ArrayList<>()));
    Comparator<Key> compare = Comparator.comparing(Key::getIndexSetSize).thenComparing(Key::getKeyLength).reversed();
    List<Key> sortedKeys = allKeys.stream().sorted(compare).collect(Collectors.toList());
    for (Key k : sortedKeys) {
        result.add(new Pair<>(k.getKey().toString(), k.getIndexSet()));
    }
    return result;
}