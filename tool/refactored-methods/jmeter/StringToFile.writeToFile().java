/**
 * Write to file
 * @return boolean true if success , false otherwise
 * @throws IOException
 */
private boolean writeToFile() throws IOException {
    String fileName = getFileName();
    String content = getContent();
    boolean append = getAppendFlag();
    Charset charset = getFileEncoding();
    if (fileName.isEmpty()) {
        log.error("File name '{}' is empty", fileName);
        return false;
    }
    log.debug("Writing {} to file {} with charset {} and append {}", content, fileName, charset, append);
    Lock lock = lockMap.computeIfAbsent(fileName, key -> new ReentrantLock());
    lock.lock();
    try {
        File file = new File(fileName);
        File fileParent = file.getParentFile();
        if (fileParent == null || (fileParent.exists() && fileParent.isDirectory() && fileParent.canWrite())) {
            try {
                FileUtils.writeStringToFile(file, content, charset, append);
            } catch (IllegalArgumentException e) {
                log.error("The file {} can't be written to", file, e);
                return false;
            }
        } else {
            log.error("The parent file of {} doesn't exist or is not writable", file);
            return false;
        }
    } finally {
        lock.unlock();
    }
    return true;
}
// ---- helper method(s) introduced by the refactoring ----
private String getFileName() {
    return ((CompoundVariable) values[0]).execute().trim();
}

private String getContent() {
    String content = ((CompoundVariable) values[1]).execute();
    return NEW_LINE_PATTERN.matcher(content).replaceAll(System.lineSeparator());
}

private boolean getAppendFlag() {
    if (values.length >= 3) {
        String appendString = ((CompoundVariable) values[2]).execute().toLowerCase(Locale.ROOT).trim();
        return !appendString.isEmpty() && Boolean.parseBoolean(appendString);
    }
    return true;
}

private Charset getFileEncoding() {
    if (values.length == 4) {
        String charsetParamValue = ((CompoundVariable) values[3]).execute();
        if (StringUtils.isNotEmpty(charsetParamValue)) {
            return Charset.forName(charsetParamValue);
        }
    }
    return StandardCharsets.UTF_8;
}

