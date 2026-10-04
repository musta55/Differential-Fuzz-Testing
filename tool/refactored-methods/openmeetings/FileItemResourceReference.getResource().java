@Override
public IResource getResource() {
    return new FileSystemResource() {

        private static final long serialVersionUID = 1L;

        private File file;

        private T r;

        @Override
        protected String getMimeType() throws IOException {
            return FileItemResourceReference.this.getMimeType(r);
        }

        @Override
        protected ResourceResponse newResourceResponse(Attributes attr) {
            r = getFileItem(attr);
            if (r == null) {
                return createErrorResponse();
            }
            file = getFile(r, attr);
            if (file == null || !file.exists()) {
                return createErrorResponse();
            }
            return createSuccessfulResponse(attr, file.toPath());
        }

        private ResourceResponse createSuccessfulResponse(Attributes attr, Path filePath) {
            ResourceResponse rr = createResourceResponse(attr, filePath);
            rr.setFileName(getFileName(r));
            return rr;
        }

        private ResourceResponse createErrorResponse() {
            log.debug("No file item was found");
            ResourceResponse rr = new ResourceResponse();
            rr.setError(HttpServletResponse.SC_NOT_FOUND);
            return rr;
        }
    };
}