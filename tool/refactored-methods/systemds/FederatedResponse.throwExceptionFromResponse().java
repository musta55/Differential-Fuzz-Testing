/**
 * Checks the data object array for exceptions that occurred in the federated worker during handling of request.
 *
 * @throws Exception the exception retrieved from the data object array or DMLRuntimeException if no exception is
 *                   provided by the federated worker.
 */
public void throwExceptionFromResponse() throws Exception {
    for (Object potentialException : _data) {
        if (potentialException != null && (potentialException instanceof Exception)) {
            throw (Exception) potentialException;
        }
    }
    String errorMessage = getErrorMessage();
    if (!errorMessage.equals("No readable error message"))
        throw new DMLRuntimeException(errorMessage);
    else
        throw new DMLRuntimeException("Unknown runtime exception in handling of federated request by federated worker.");
}
// ---- helper method(s) introduced by the refactoring ----
private long calculateDataSize(Object[] data) {
    long size = 0;
    for (Object obj : data) {
        if (obj instanceof CacheBlock)
            size += ((CacheBlock<?>) obj).getExactSerializedSize();
    }
    return size;
}

