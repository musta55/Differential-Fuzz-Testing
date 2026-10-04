public ProcessResultList convertDocument(FileItem f, File pdf, ProcessResultList logs, Optional<DoubleConsumer> progress) throws IOException {
    log.debug("convertDocument");
    List<String> argv = List.of(getPathToConvert(), "-density", getDpi(), "-define", "pdf:use-cropbox=true", pdf.getCanonicalPath(), "+profile", "'*'", "-quality", getQuality(), new File(pdf.getParentFile(), PAGE_TMPLT).getCanonicalPath());
    ProcessResult res = ProcessHelper.exec("convert PDF to images", argv);
    logs.add(res);
    updateProgress(progress, 0.25);
    if (res.isOk()) {
        File[] pages = pdf.getParentFile().listFiles(fi -> fi.isFile() && fi.getName().startsWith(DOC_PAGE_PREFIX) && fi.getName().endsWith(EXTENSION_PNG));
        if (pages == null || pages.length == 0) {
            f.setCount(0);
        } else {
            f.setCount(pages.length);
            logs.add(initSize(f, pages[0], PNG_MIME_TYPE));
        }
    }
    updateProgress(progress, 0.25);
    return logs;
}
// ---- helper method(s) introduced by the refactoring ----
private void updateProgress(Optional<DoubleConsumer> progress, double step) {
    progress.ifPresent(theProgress -> theProgress.accept(step));
}

