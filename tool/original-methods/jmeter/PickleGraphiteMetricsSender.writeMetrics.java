private void writeMetrics(List<MetricTuple> currentMetrics) {
    SocketOutputStream out = null;
    try {
        String payload = convertMetricsToPickleFormat(currentMetrics);
        int length = payload.length();
        byte[] header = ByteBuffer.allocate(4).putInt(length).array();
        out = socketOutputStreamPool.borrowObject(socketConnectionInfos);
        out.write(header);
        // pickleWriter is not closed as it would close the underlying pooled out
        Writer pickleWriter = new OutputStreamWriter(out, CHARSET_NAME);
        pickleWriter.write(payload);
        pickleWriter.flush();
        socketOutputStreamPool.returnObject(socketConnectionInfos, out);
    } catch (Exception e) {
        // if there was an error, we might miss some data, for now, drop those and try to keep going.
        if (out != null) {
            try {
                socketOutputStreamPool.invalidateObject(socketConnectionInfos, out);
            } catch (Exception e1) {
                log.warn("Exception invalidating socketOutputStream connected to graphite server {}:{}", socketConnectionInfos.getHost(), socketConnectionInfos.getPort(), e1);
            }
        }
        log.error("Error writing to Graphite: {}", e.getMessage(), e);
    }
    if (log.isDebugEnabled()) {
        log.debug("Wrote {} metrics", currentMetrics.size());
    }
}