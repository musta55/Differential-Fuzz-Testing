@Override
public void setup(String influxdbUrl, String influxDBToken) throws Exception {
    try {
        log.debug("Setting up with url:{}", influxdbUrl);
        String[] urlComponents = influxdbUrl.split(":");
        if (urlComponents.length == 2) {
            hostAddress = InetAddress.getByName(urlComponents[0]);
            udpPort = Integer.parseInt(urlComponents[1]);
        } else {
            throw new IllegalArgumentException("InfluxDB url '" + influxdbUrl + "' is wrong. The format should be <host/ip>:<port>");
        }
    } catch (Exception e) {
        throw new IllegalArgumentException("InfluxDB url '" + influxdbUrl + "' is wrong. The format should be <host/ip>:<port>", e);
    }
}