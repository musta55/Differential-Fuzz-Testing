private long connect(long timeMillis) throws IOException {
    String uriStr = fsRecoveryHandler.readConnectUri();
    if (!uriStr.equals(lastConnectURI)) {
        LOG.debug("Got new RPC connect address {}", uriStr);
        lastConnectURI = uriStr;
        if (umbilical != null) {
            RPC.stopProxy(umbilical);
        }
        retryTimeoutMillis = Long.getLong(RETRY_TIMEOUT, RETRY_TIMEOUT_DEFAULT);
        retryDelayMillis = Long.getLong(RETRY_DELAY, RETRY_DELAY_DEFAULT);
        rpcTimeout = Integer.getInteger(RPC_TIMEOUT, RPC_TIMEOUT_DEFAULT);
        URI heartbeatUri = URI.create(uriStr);
        parseQueryParameters(heartbeatUri);
        InetSocketAddress address = NetUtils.createSocketAddrForHost(heartbeatUri.getHost(), heartbeatUri.getPort());
        umbilical = RPC.getProxy(StreamingContainerUmbilicalProtocol.class, StreamingContainerUmbilicalProtocol.versionID, address, currentUser, conf, defaultSocketFactory, rpcTimeout);
        // reset timeout
        return System.currentTimeMillis() + retryTimeoutMillis;
    }
    return timeMillis;
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

