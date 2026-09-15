public static URI toConnectURI(InetSocketAddress address, int rpcTimeoutMillis, long retryDelayMillis, long retryTimeoutMillis) throws Exception {
    return new URIBuilder().setScheme("stram").setHost(address.getHostName()).setPort(address.getPort()).setParameter(QP_RPC_TIMEOUT, Integer.toString(rpcTimeoutMillis)).setParameter(QP_RETRY_DELAY_MILLIS, Long.toString(retryDelayMillis)).setParameter(QP_RETRY_TIMEOUT_MILLIS, Long.toString(retryTimeoutMillis)).build();
}
// ---- helper method(s) introduced by the refactoring ----
private void parseQueryParameters(URI uri) {
    String queryStr = uri.getQuery();
    if (queryStr != null) {
        List<NameValuePair> queryList = URLEncodedUtils.parse(queryStr, Charset.defaultCharset());
        for (NameValuePair pair : queryList) {
            String value = pair.getValue();
            String key = pair.getName();
            if (QP_RPC_TIMEOUT.equals(key)) {
                this.rpcTimeout = Integer.parseInt(value);
            } else if (QP_RETRY_TIMEOUT_MILLIS.equals(key)) {
                this.retryTimeoutMillis = Long.parseLong(value);
            } else if (QP_RETRY_DELAY_MILLIS.equals(key)) {
                this.retryDelayMillis = Long.parseLong(value);
            }
        }
    }
}

private Throwable unwrapThrowable(Throwable t) {
    while (t instanceof InvocationTargetException || t instanceof UndeclaredThrowableException) {
        Throwable cause = t.getCause();
        if (cause != null) {
            t = cause;
        }
    }
    return t;
}

