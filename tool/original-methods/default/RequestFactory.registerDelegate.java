public void registerDelegate(StramToNodeRequest.RequestType requestType, RequestDelegate delegate) {
    RequestDelegate old = map.put(requestType, delegate);
    if (old != null) {
        logger.warn("Replacing delegate {} for {} by {}", new Object[] { old, requestType, delegate });
    }
}