protected static boolean isPartialMatch(String host) {
    for (String suffix : nonProxyHostSuffix) {
        if (host.endsWith(suffix)) {
            return true;
        }
    }
    return false;
}