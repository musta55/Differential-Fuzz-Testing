@Override
public IResource getResource() {
    return new AbstractResource() {

        private static final long serialVersionUID = 1L;

        @Override
        protected ResourceResponse newResourceResponse(Attributes attributes) {
            ResourceResponse response = new ResourceResponse();
            ServletWebRequest webRequest = (ServletWebRequest) attributes.getRequest();
            try {
                MultipartServletWebRequest multiPartRequest = webRequest.newMultipartWebRequest(Bytes.bytes(getMaxUploadSize()), "ignored");
                multiPartRequest.parseFileParts();
                String sid = multiPartRequest.getPostParameters().getParameterValue(PARAM_SID_NAME).toString();
                Client c = cm.getBySid(sid);
                long langId = getLangId(c);
                if (isUploadAllowed(c)) {
                    Map<String, List<FileItem>> files = multiPartRequest.getFiles();
                    List<FileItem> fileItems = files.get(PARAM_FILE_NAME);
                    String uuid = randomUUID().toString();
                    processFiles(c, fileItems, uuid, multiPartRequest);
                    prepareSuccessResponse(response, uuid, langId);
                } else {
                    prepareErrorResponse(response, langId, "access.denied.header");
                }
            } catch (Exception e) {
                log.error("An error occurred while uploading a file", e);
                prepareErrorResponse(response, null, e.getMessage());
            }
            return response;
        }
    };
}
// ---- helper method(s) introduced by the refactoring ----
private void prepareSuccessResponse(ResourceResponse response, String uuid, long langId) {
    prepareResponse(response, Status.SUCCESS, uuid, Application.getString("54", langId));
}

private void prepareErrorResponse(ResourceResponse response, Long langId, String messageKey) {
    String message = langId != null ? Application.getString(messageKey, langId) : messageKey;
    prepareResponse(response, Status.ERROR, null, message);
}

