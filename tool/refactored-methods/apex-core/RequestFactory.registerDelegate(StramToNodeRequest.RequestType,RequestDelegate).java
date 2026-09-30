public void registerDelegate(StramToNodeRequest.RequestType requestType, RequestDelegate delegate) {
    if (requestType == null) {
        throw new IllegalArgumentException("requestType cannot be null");
    }
    if (delegate == null) {
        throw new IllegalArgumentException("delegate cannot be null");
    }
    RequestDelegate old = map.put(requestType, delegate);
    if (old != null) {
        logger.warn("Replacing delegate {} for {} with {}", old, requestType, delegate);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private RequestDelegate getDelegateForRequestType(StramToNodeRequest.RequestType requestType) {
    return map.get(requestType);
}

private OperatorRequest getFallbackRequestExecutor(StramToNodeRequest snr) {
    return (snr.cmd != null) ? snr.cmd : null;
}

