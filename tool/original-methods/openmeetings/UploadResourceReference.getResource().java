@Override
public IResource getResource() {
    return new AbstractResource() {

        private static final long serialVersionUID = 1L;

        @Override
        protected ResourceResponse newResourceResponse(Attributes attributes) {
            final ResourceResponse response = new ResourceResponse();
            final ServletWebRequest webRequest = (ServletWebRequest) attributes.getRequest();
            try {
                MultipartServletWebRequest multiPartRequest = webRequest.newMultipartWebRequest(Bytes.bytes(getMaxUploadSize()), "ignored");
                multiPartRequest.parseFileParts();
                String sid = multiPartRequest.getPostParameters().getParameterValue(PARAM_SID_NAME).toString();
                Client c = cm.getBySid(sid);
                final long langId = getLangId(c);
                if (isUploadAllowed(c)) {
                    Map<String, List<FileItem>> files = multiPartRequest.getFiles();
                    final List<FileItem> fileItems = files.get(PARAM_FILE_NAME);
                    final String uuid = randomUUID().toString();
                    processFiles(c, fileItems, uuid, multiPartRequest);
                    prepareResponse(response, Status.SUCCESS, uuid, Application.getString("54", langId));
                } else {
                    prepareResponse(response, Status.ERROR, null, Application.getString("access.denied.header", langId));
                }
            } catch (Exception e) {
                log.error("An error occurred while uploading a file", e);
                prepareResponse(response, Status.ERROR, null, e.getMessage());
            }
            return response;
        }
    };
}