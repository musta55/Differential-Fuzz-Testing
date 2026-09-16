/**
 * Converts PDF document to the series of images
 *
 * @param f - {@link FileItem} object to write number of pages and size
 * @param pdf - input PDF document
 * @param logs - logs of the conversion
 * @return - result of conversion
 * @throws IOException in case IO exception occurred
 */
public ProcessResultList convertDocument(FileItem f, File pdf, ProcessResultList logs, Optional<DoubleConsumer> progress) throws IOException {
    log.debug("convertDocument");
    List<String> argv = List.of(getPathToConvert(), "-density", getDpi(), "-define", "pdf:use-cropbox=true", pdf.getCanonicalPath(), "+profile", "'*'", "-quality", getQuality(), new File(pdf.getParentFile(), PAGE_TMPLT).getCanonicalPath());
    ProcessResult res = ProcessHelper.exec("convert PDF to images", argv);
    logs.add(res);
    progress.ifPresent(theProgress -> theProgress.accept(1. / 4));
    if (res.isOk()) {
        File[] pages = pdf.getParentFile().listFiles(fi -> fi.isFile() && fi.getName().startsWith(DOC_PAGE_PREFIX) && fi.getName().endsWith(EXTENSION_PNG));
        if (pages == null || pages.length == 0) {
            f.setCount(0);
        } else {
            f.setCount(pages.length);
            logs.add(initSize(f, pages[0], PNG_MIME_TYPE));
        }
    }
    progress.ifPresent(theProgress -> theProgress.accept(1. / 4));
    return logs;
}