@Override
protected void processFiles(Client c, List<FileItem> fileItems, String uuid, MultipartServletWebRequest multiPartRequest) {
    startRunnable(() -> processFileUpload(c, fileItems, uuid), getApplicationName() + " - Restore");
}
// ---- helper method(s) introduced by the refactoring ----
private void processFileUpload(Client c, List<FileItem> fileItems, String uuid) {
    final AtomicInteger lastProgress = new AtomicInteger(0);
    final AtomicInteger progress = new AtomicInteger(0);
    if (fileItems.isEmpty()) {
        sendError(c, uuid, "File is empty");
        return;
    }
    Timer timer = new Timer();
    try {
        FileItem fileItem = fileItems.get(0);
        if (fileItem.getInputStream() == null) {
            sendError(c, uuid, "File is empty");
            return;
        }
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                sendProgress(c, uuid, lastProgress, progress.get());
            }
        }, 0, 1000);
        backupImport.performImport(fileItem.getInputStream(), progress);
    } catch (Exception e) {
        log.error("Exception on panel backup download ", e);
        sendError(c, uuid, e.getMessage() == null ? "Unexpected error" : e.getMessage());
    } finally {
        fileItems.forEach(FileItem::delete);
        timer.cancel();
    }
    sendProgress(c, uuid, lastProgress, 100);
}

