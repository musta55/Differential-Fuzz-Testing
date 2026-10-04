protected static boolean isPartialMatch(String host) {
    for (int i = 0; i < NON_PROXY_HOST_SUFFIX_SIZE; i++) {
        if (host.endsWith(nonProxyHostSuffix.get(i))) {
            return true;
        }
    }
    return false;
}