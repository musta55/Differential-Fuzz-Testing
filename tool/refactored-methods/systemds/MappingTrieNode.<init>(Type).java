public MappingTrieNode(Type nodeType) {
    this.nodeType = nodeType;
    this.children = new HashMap<>();
    this.rowIndexes = new ArrayList<>();
}