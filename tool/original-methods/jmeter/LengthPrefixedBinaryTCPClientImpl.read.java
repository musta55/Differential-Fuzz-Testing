/**
 * {@inheritDoc}
 */
@Override
public String read(InputStream is, SampleResult sampleResult) throws ReadException {
    byte[] msg = new byte[0];
    int msgLen = 0;
    byte[] lengthBuffer = new byte[lengthPrefixLen];
    try {
        if (is.read(lengthBuffer, 0, lengthPrefixLen) == lengthPrefixLen) {
            sampleResult.latencyEnd();
            msgLen = byteArrayToInt(lengthBuffer);
            msg = new byte[msgLen];
            int bytes = JOrphanUtils.read(is, msg, 0, msgLen);
            if (bytes < msgLen) {
                log.warn("Incomplete message read, expected: {} got: {}", msgLen, bytes);
            }
        }
        String buffer = JOrphanUtils.baToHexString(msg);
        if (log.isDebugEnabled()) {
            log.debug("Read: " + msgLen + "\n" + buffer);
        }
        return buffer;
    } catch (IOException e) {
        throw new ReadException("", e, JOrphanUtils.baToHexString(msg));
    }
}