public MappingTrieNode(Type nodeType) {
    this.nodeType = nodeType;
    children = new HashMap<>();
    rowIndexes = new ArrayList<>();
}