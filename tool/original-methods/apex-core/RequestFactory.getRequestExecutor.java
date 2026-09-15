/**
 * Process request from stram for further communication through the protocol. Extended reporting is on a per node basis (won't occur under regular operation)
 *
 * @param node - Node which will be handling this request.
 * @param snr - The serialized request which contains context for the request.
 * @return - The actual object which will handle the request.
 */
public OperatorRequest getRequestExecutor(final Node<?> node, final StramToNodeRequest snr) {
    RequestDelegate delegate = map.get(snr.requestType);
    if (delegate == null) {
        if (snr.cmd != null) {
            return snr.cmd;
        }
        return null;
    }
    return delegate.getRequestExecutor(node, snr);
}