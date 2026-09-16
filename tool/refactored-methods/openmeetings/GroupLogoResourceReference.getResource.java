@Override
public IResource getResource() {
    return new FileSystemResource() {

        private static final long serialVersionUID = 1L;

        @Override
        protected String getMimeType() throws IOException {
            return PNG_MIME_TYPE;
        }

        @Override
        protected ResourceResponse newResourceResponse(Attributes attrs) {
            Long id = extractGroupId(attrs.getParameters());
            boolean allowed = isAccessAllowed(id);
            if (allowed) {
                return createResourceResponse(attrs, getGroupLogo(id, true).toPath());
            } else {
                log.debug("Not authorized");
                ResourceResponse rr = new ResourceResponse();
                rr.setError(HttpServletResponse.SC_FORBIDDEN);
                return rr;
            }
        }
    };
}
// ---- helper method(s) introduced by the refactoring ----
private Long extractGroupId(PageParameters params) {
    StringValue inId = params.get("id");
    try {
        return inId.toOptionalLong();
    } catch (Exception e) {
        // no-op expected
        return null;
    }
}

private boolean isAccessAllowed(Long id) {
    WebSession ws = WebSession.get();
    if (!ws.isSignedIn()) {
        return false;
    }
    boolean allowed = id == null || hasAdminLevel(getRights()) || groupUserDao.getByGroupAndUser(id, getUserId()) != null;
    if (!allowed && ws.getInvitation() != null) {
        Room r = ws.getInvitation().getRoom() == null ? null : roomDao.get(ws.getInvitation().getRoom().getId());
        if (r != null && r.getGroups() != null) {
            for (RoomGroup rg : r.getGroups()) {
                if (rg.getGroup().getId().equals(id)) {
                    allowed = true;
                    break;
                }
            }
        }
    }
    return allowed;
}

