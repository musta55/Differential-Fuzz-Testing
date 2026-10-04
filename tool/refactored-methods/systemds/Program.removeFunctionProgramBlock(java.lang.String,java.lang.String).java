public synchronized void removeFunctionProgramBlock(String namespace, String fname) {
    namespace = getSafeNamespace(namespace);
    FunctionDictionary<FunctionProgramBlock> dict = _namespaces.get(namespace);
    if (dict != null && dict.containsFunction(fname)) {
        dict.removeFunction(fname);
    }
}