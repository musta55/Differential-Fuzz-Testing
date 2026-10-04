/**
 * {@inheritDoc}
 */
@Override
public String read(InputStream is, SampleResult sampleResult) throws ReadException {
    byte[] lengthBuffer = new byte[lengthPrefixLen];
    try {
        if (is.read(lengthBuffer, 0, lengthPrefixLen) != lengthPrefixLen) {
            throw new ReadException("Failed to read length prefix", null, "");
        }
        sampleResult.latencyEnd();
        int msgLen = byteArrayToInt(lengthBuffer);
        byte[] msg = new byte[msgLen];
        int bytesRead = JOrphanUtils.read(is, msg, 0, msgLen);
        if (bytesRead < msgLen) {
            log.warn("Incomplete message read, expected: {} got: {}", msgLen, bytesRead);
        }
        String buffer = JOrphanUtils.baToHexString(msg);
        if (log.isDebugEnabled()) {
            log.debug("Read: " + msgLen + "\n" + buffer);
        }
        return buffer;
    } catch (IOException e) {
        throw new ReadException("Error reading message", e, "");
    }
}