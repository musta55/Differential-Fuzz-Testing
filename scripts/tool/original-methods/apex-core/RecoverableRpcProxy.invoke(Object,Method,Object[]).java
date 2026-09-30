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
            // handle RPC failure
            while (t instanceof InvocationTargetException || t instanceof UndeclaredThrowableException) {
                Throwable cause = t.getCause();
                if (cause != null) {
                    t = cause;
                }
            }
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