@Override
public IResource getResource() {
    return new ByteArrayResource(PNG_MIME_TYPE) {

        private static final long serialVersionUID = 1L;

        private Long userId = null;

        private String uri = null;

        @Override
        protected ResourceResponse newResourceResponse(Attributes attributes) {
            ResourceResponse rr;
            if (WebSession.get().isSignedIn()) {
                PageParameters params = attributes.getParameters();
                try {
                    userId = params.get("id").toOptionalLong();
                    uri = SIP_USER_ID.equals(userId) ? null : userDao.get(userId).getPictureUri();
                } catch (Exception e) {
                    // no-op, junk filter
                }
                rr = super.newResourceResponse(attributes);
                rr.disableCaching();
            } else {
                log.debug("Not authorized");
                rr = new ResourceResponse();
                rr.setError(HttpServletResponse.SC_FORBIDDEN);
            }
            return rr;
        }

        @Override
        protected byte[] getData(Attributes attributes) {
            if (isRelative(uri)) {
                File img = OmFileHelper.getUserProfilePicture(userId, uri);
                try (InputStream is = new FileInputStream(img)) {
                    return IOUtils.toByteArray(is);
                } catch (Exception e) {
                    log.error("failed to get bytes from image", e);
                }
            }
            return null;
        }
    };
}