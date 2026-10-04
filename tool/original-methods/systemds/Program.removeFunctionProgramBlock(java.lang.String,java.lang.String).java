public synchronized void removeFunctionProgramBlock(String namespace, String fname) {
    namespace = getSafeNamespace(namespace);
    FunctionDictionary<?> dict = null;
    if (_namespaces.containsKey(namespace)) {
        dict = _namespaces.get(namespace);
        if (dict.containsFunction(fname))
            dict.removeFunction(fname);
    }
}