/**
 * Adapted from DavMethodBase to handle MultiStatus responses.
 *
 * @param response {@link HttpResponse} to be converted to {@link MultiStatus}
 * @return MultiStatus response
 * @throws DavException if the response body could not be parsed
 */
@Override
public MultiStatus getResponseBodyAsMultiStatus(HttpResponse response) throws DavException {
    processResponseIfNotDone(response);
    if (multiStatus != null) {
        return multiStatus;
    }
    DavException dx = getResponseException(response);
    if (dx != null) {
        throw dx;
    }
    throw new DavException(response.getStatusLine().getStatusCode(), getMethod() + " resulted with unexpected status: " + response.getStatusLine());
}
// ---- helper method(s) introduced by the refactoring ----
private SyncMethod(URI uri, SyncReportInfo reportInfo, int depth) throws IOException {
    super(uri);
    setEntity(XmlEntity.create(reportInfo));
    if (depth >= 0) {
        setDepth(depth);
    }
    log.info("Using the WEBDAV-SYNC method for syncing.");
}

private void processResponseIfNotDone(HttpResponse response) {
    if (!processedResponse && succeeded(response)) {
        try {
            Document document = getResponseBodyAsDocument(response.getEntity());
            if (document != null) {
                synctoken = DomUtil.getChildText(document.getDocumentElement(), SyncReportInfo.XML_SYNC_TOKEN, DavConstants.NAMESPACE);
                log.info("Sync-Token for REPORT: {}", synctoken);
                multiStatus = MultiStatus.createFromXml(document.getDocumentElement());
            }
        } catch (IOException e) {
            log.error("Error while parsing sync-token.", e);
        }
        processedResponse = true;
    }
}

