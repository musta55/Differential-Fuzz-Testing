public ProcessResultList convertImage(BaseFileItem f, StoredFile sf, ProcessResultList logs, Optional<DoubleConsumer> progress) throws IOException {
    File png = f.getFile(EXTENSION_PNG);
    if (!sf.isPng()) {
        File img = f.getFile(sf.getExt());
        log.debug("##### convertImage destinationFile: {}", png);
        logs.add(convertSinglePng(img, png));
    } else if (!png.exists()) {
        copyFile(f.getFile(sf.getExt()), png);
    }
    progress.ifPresent(theProgress -> theProgress.accept(HALF_STEP));
    logs.add(initSize(f, png, PNG_MIME_TYPE));
    progress.ifPresent(theProgress -> theProgress.accept(HALF_STEP));
    return logs;
}