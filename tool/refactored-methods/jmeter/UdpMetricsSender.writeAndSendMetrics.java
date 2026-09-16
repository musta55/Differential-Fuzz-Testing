@Override
public void writeAndSendMetrics() {
    List<MetricTuple> tempMetrics = extractMetrics();
    if (!tempMetrics.isEmpty()) {
        String metricsData = buildMetricsData(tempMetrics);
        sendMetrics(metricsData);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void parseUrl(String influxdbUrl) throws Exception {
    String[] urlComponents = influxdbUrl.split(":");
    if (urlComponents.length == 2) {
        hostAddress = InetAddress.getByName(urlComponents[0]);
        udpPort = Integer.parseInt(urlComponents[1]);
    } else {
        throw new IllegalArgumentException("InfluxDB url '" + influxdbUrl + "' is wrong. The format should be <host/ip>:<port>");
    }
}

private List<MetricTuple> extractMetrics() {
    synchronized (lock) {
        if (metrics.isEmpty()) {
            return new ArrayList<>();
        }
        List<MetricTuple> tempMetrics = metrics;
        metrics = new ArrayList<>(tempMetrics.size());
        return tempMetrics;
    }
}

private static String buildMetricsData(List<MetricTuple> copyMetrics) {
    StringBuilder sb = new StringBuilder(copyMetrics.size() * 35);
    for (MetricTuple metric : copyMetrics) {
        sb.append(metric.measurement).append(metric.tag).append(" ").append(metric.field).append(" ").append(metric.timestamp + "000000").append("\n");
    }
    return sb.toString();
}

private void sendMetrics(String metricsData) {
    try (DatagramSocket ds = new DatagramSocket()) {
        byte[] buf = metricsData.getBytes(StandardCharsets.UTF_8);
        DatagramPacket dp = new DatagramPacket(buf, buf.length, this.hostAddress, this.udpPort);
        ds.send(dp);
    } catch (SocketException e) {
        log.error("Cannot open udp port!", e);
    } catch (IOException e) {
        log.error("Error in transferring udp package", e);
    }
}

