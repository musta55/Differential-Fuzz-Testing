public String getNamespace() {
    if (Strings.isNullOrEmpty(namespace)) {
        return null;
    }
    if (!namespace.endsWith("/")) {
        return namespace + "/";
    }
    return namespace;
}