public SyncMethod(URI uri, SyncReportInfo reportInfo) throws IOException {
    this(uri, reportInfo, reportInfo.getDepth());
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

