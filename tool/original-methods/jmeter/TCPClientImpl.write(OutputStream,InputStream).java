/**
 * {@inheritDoc}
 */
@Override
public void write(OutputStream os, InputStream is) throws IOException {
    byte[] buff = new byte[512];
    while (is.read(buff) > 0) {
        if (log.isDebugEnabled()) {
            log.debug("WriteIS: {}", showEOL(new String(buff, CHARSET)));
        }
        os.write(buff);
        os.flush();
    }
}