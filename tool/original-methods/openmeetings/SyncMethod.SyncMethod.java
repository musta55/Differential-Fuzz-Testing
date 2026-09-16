public SyncMethod(URI uri, SyncReportInfo reportInfo) throws IOException {
    super(uri);
    setEntity(XmlEntity.create(reportInfo));
    if (reportInfo.getDepth() >= 0) {
        parseDepth(reportInfo.getDepth());
    }
    log.info("Using the WEBDAV-SYNC method for syncing.");
}