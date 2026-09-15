/**
 * Process request from stram for further communication through the protocol. Extended reporting is on a per node basis (won't occur under regular operation)
 *
 * @param node - Node which will be handling this request.
 * @param snr - The serialized request which contains context for the request.
 * @return - The actual object which will handle the request.
 */
public OperatorRequest getRequestExecutor(final Node<?> node, final StramToNodeRequest snr) {
    if (node == null) {
        throw new IllegalArgumentException("node cannot be null");
    }
    if (snr == null) {
        throw new IllegalArgumentException("snr cannot be null");
    }
    RequestDelegate delegate = getDelegateForRequestType(snr.requestType);
    if (delegate == null) {
        return getFallbackRequestExecutor(snr);
    }
    return delegate.getRequestExecutor(node, snr);
}
// ---- helper method(s) introduced by the refactoring ----
private RequestDelegate getDelegateForRequestType(StramToNodeRequest.RequestType requestType) {
    return map.get(requestType);
}

private OperatorRequest getFallbackRequestExecutor(StramToNodeRequest snr) {
    return (snr.cmd != null) ? snr.cmd : null;
}

