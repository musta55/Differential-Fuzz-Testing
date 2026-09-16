/**
 * {@inheritDoc}
 */
@Override
public void write(OutputStream os, InputStream is) throws IOException {
    byte[] buff = new byte[512];
    int bytesRead;
    while ((bytesRead = is.read(buff)) > 0) {
        if (log.isDebugEnabled()) {
            log.debug("WriteIS: {}", showEOL(new String(buff, 0, bytesRead, CHARSET)));
        }
        os.write(buff, 0, bytesRead);
        os.flush();
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void initializeEolByte() {
    setEolByte(EOL_INT);
    if (useEolByte) {
        log.info("Using eolByte={}", eolByte);
    }
}

private void initializeCharset() {
    setCharset(CHARSET);
    String configuredCharset = JMeterUtils.getProperty("tcp.charset");
    if (StringUtils.isEmpty(configuredCharset)) {
        log.info("Using platform default charset:{}", CHARSET);
    } else {
        log.info("Using charset:{}", configuredCharset);
    }
}

