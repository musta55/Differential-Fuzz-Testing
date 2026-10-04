private void writeMetrics(List<MetricTuple> currentMetrics) {
    SocketOutputStream out = null;
    try {
        out = socketOutputStreamPool.borrowObject(socketConnectionInfos);
        // pw is not closed as it would close the underlying pooled out
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(out, CHARSET_NAME), false);
        for (MetricTuple metric : currentMetrics) {
            pw.printf("%s %s %d%n", metric.name, metric.value, metric.timestamp);
        }
        pw.flush();
        if (log.isDebugEnabled()) {
            log.debug("Wrote {} metrics", currentMetrics.size());
        }
        if (pw.checkError()) {
            socketOutputStreamPool.invalidateObject(socketConnectionInfos, out);
            log.error("IO Errors writing to Graphite, some data will be lost");
        } else {
            socketOutputStreamPool.returnObject(socketConnectionInfos, out);
        }
    } catch (Exception e) {
        // if there was an error, we might miss some data. for now, drop those try to keep going.
        if (out != null) {
            try {
                socketOutputStreamPool.invalidateObject(socketConnectionInfos, out);
            } catch (Exception e1) {
                log.warn("Exception invalidating socketOutputStream connected to graphite server {}:{}", socketConnectionInfos.getHost(), socketConnectionInfos.getPort(), e1);
            }
        }
        log.error("Error writing to Graphite: {}", e.getMessage(), e);
    }
}