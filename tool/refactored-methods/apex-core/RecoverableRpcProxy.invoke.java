@Override
@SuppressWarnings("SleepWhileInLoop")
public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    long endTimeMillis = System.currentTimeMillis() + retryTimeoutMillis;
    if (umbilical == null) {
        endTimeMillis = connect(endTimeMillis);
    }
    while (true) {
        if (umbilical == null) {
            throw new IOException("RecoverableRpcProxy is closed.");
        }
        try {
            return method.invoke(umbilical, args);
        } catch (Throwable t) {
            t = unwrapThrowable(t);
            final long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis < endTimeMillis) {
                LOG.warn("RPC failure, will retry after {} ms (remaining {} ms)", retryDelayMillis, endTimeMillis - currentTimeMillis, t);
                sleep(retryDelayMillis);
                endTimeMillis = connect(endTimeMillis);
            } else {
                LOG.error("Giving up RPC connection recovery after {} ms", currentTimeMillis - endTimeMillis + retryTimeoutMillis, t);
                close();
                throw t;
            }
        }
    }
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

