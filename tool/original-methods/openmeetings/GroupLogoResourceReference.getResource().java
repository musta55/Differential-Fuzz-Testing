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
            Long id = null;
            boolean allowed = false;
            WebSession ws = WebSession.get();
            if (ws.isSignedIn()) {
                PageParameters params = attrs.getParameters();
                StringValue inId = params.get("id");
                try {
                    id = inId.toOptionalLong();
                } catch (Exception e) {
                    //no-op expected
                }
                allowed = id == null || hasAdminLevel(getRights()) || null != groupUserDao.getByGroupAndUser(id, getUserId());
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
            }
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