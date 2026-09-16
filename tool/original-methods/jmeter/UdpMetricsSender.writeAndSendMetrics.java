@Override
public void writeAndSendMetrics() {
    List<MetricTuple> tempMetrics;
    synchronized (lock) {
        if (metrics.isEmpty()) {
            return;
        }
        tempMetrics = metrics;
        metrics = new ArrayList<>(tempMetrics.size());
    }
    final List<MetricTuple> copyMetrics = tempMetrics;
    if (!copyMetrics.isEmpty()) {
        StringBuilder sb = new StringBuilder(copyMetrics.size() * 35);
        for (MetricTuple metric : copyMetrics) {
            // Add TimeStamp in nanosecond from epoch ( default in InfluxDB
            // )
            //$NON-NLS-1$
            sb.append(metric.measurement).append(metric.tag).append(" ").append(metric.field).append(" ").append(metric.timestamp + "000000").append(//$NON-NLS-3$
            "\n");
        }
        try (DatagramSocket ds = new DatagramSocket()) {
            byte[] buf = sb.toString().getBytes(StandardCharsets.UTF_8);
            DatagramPacket dp = new DatagramPacket(buf, buf.length, this.hostAddress, this.udpPort);
            ds.send(dp);
        } catch (SocketException e) {
            log.error("Cannot open udp port!", e);
            return;
        } catch (IOException e) {
            log.error("Error in transferring udp package", e);
        } finally {
            // We drop metrics in all cases
            copyMetrics.clear();
        }
    }
}