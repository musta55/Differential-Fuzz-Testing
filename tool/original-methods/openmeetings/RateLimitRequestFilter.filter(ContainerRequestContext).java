@Override
public void filter(ContainerRequestContext context) {
    HttpSession session = request.getSession(false);
    if (session == null) {
        context.abortWith(Response.status(Status.FORBIDDEN).build());
        return;
    }
    List<String> typeList = context.getUriInfo().getQueryParameters().get("type");
    if (typeList != null && !typeList.isEmpty()) {
        TestType type = NetTestWebService.getTypeByString(typeList.get(0));
        if (TestType.PING == type || TestType.JITTER == type) {
            return;
        }
    }
    if (NetTestWebService.CLIENT_COUNT.get() > NetTestWebService.getMaxClients()) {
        log.error("Download: Max client count reached");
        context.abortWith(Response.status(Status.TOO_MANY_REQUESTS).build());
        return;
    }
    Long lastAccessed = (Long) session.getAttribute(ATTR_LAST_ACCESS_TIME);
    session.setAttribute(ATTR_LAST_ACCESS_TIME, System.currentTimeMillis());
    if (lastAccessed != null && System.currentTimeMillis() - lastAccessed.longValue() < ALLOWED_TIME) {
        context.abortWith(Response.status(Status.TOO_MANY_REQUESTS).build());
    }
}