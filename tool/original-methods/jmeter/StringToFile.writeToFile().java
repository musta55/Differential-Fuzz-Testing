/**
 * Write to file
 * @return boolean true if success , false otherwise
 * @throws IOException
 */
private boolean writeToFile() throws IOException {
    String fileName = ((CompoundVariable) values[0]).execute().trim();
    String content = ((CompoundVariable) values[1]).execute();
    boolean append = true;
    if (values.length >= 3) {
        String appendString = ((CompoundVariable) values[2]).execute().toLowerCase(Locale.ROOT).trim();
        if (!appendString.isEmpty()) {
            append = Boolean.parseBoolean(appendString);
        }
    }
    content = NEW_LINE_PATTERN.matcher(content).replaceAll(System.lineSeparator());
    Charset charset = StandardCharsets.UTF_8;
    if (values.length == 4) {
        String charsetParamValue = ((CompoundVariable) values[3]).execute();
        if (StringUtils.isNotEmpty(charsetParamValue)) {
            charset = Charset.forName(charsetParamValue);
        }
    }
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