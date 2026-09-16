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
            if (r != null) {
                file = getFile(r, attr);
            }
            if (file != null && file.exists()) {
                ResourceResponse rr = createResourceResponse(attr, file.toPath());
                rr.setFileName(getFileName(r));
                return rr;
            } else {
                log.debug("No file item was found");
                ResourceResponse rr = new ResourceResponse();
                rr.setError(HttpServletResponse.SC_NOT_FOUND);
                return rr;
            }
        }
    };
}