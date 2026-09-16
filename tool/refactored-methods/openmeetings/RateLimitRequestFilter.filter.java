@Override
public void filter(ContainerRequestContext context) {
    HttpSession session = request.getSession(false);
    if (session == null) {
        abortRequest(context, Status.FORBIDDEN);
        return;
    }
    if (isExemptRequest(context)) {
        return;
    }
    if (isMaxClientCountReached()) {
        log.error("Download: Max client count reached");
        abortRequest(context, Status.TOO_MANY_REQUESTS);
        return;
    }
    if (isRateLimitExceeded(session)) {
        abortRequest(context, Status.TOO_MANY_REQUESTS);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private boolean isExemptRequest(ContainerRequestContext context) {
    List<String> typeList = context.getUriInfo().getQueryParameters().get("type");
    return typeList != null && !typeList.isEmpty() && (TestType.PING == NetTestWebService.getTypeByString(typeList.get(0)) || TestType.JITTER == NetTestWebService.getTypeByString(typeList.get(0)));
}

private boolean isMaxClientCountReached() {
    return NetTestWebService.CLIENT_COUNT.get() > NetTestWebService.getMaxClients();
}

private boolean isRateLimitExceeded(HttpSession session) {
    Long lastAccessed = (Long) session.getAttribute(ATTR_LAST_ACCESS_TIME);
    session.setAttribute(ATTR_LAST_ACCESS_TIME, System.currentTimeMillis());
    return lastAccessed != null && System.currentTimeMillis() - lastAccessed.longValue() < ALLOWED_TIME;
}

private void abortRequest(ContainerRequestContext context, Status status) {
    context.abortWith(Response.status(status).build());
}

