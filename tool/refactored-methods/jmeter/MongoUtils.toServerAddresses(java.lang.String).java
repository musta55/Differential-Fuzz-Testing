public static List<ServerAddress> toServerAddresses(String connections) throws UnknownHostException {
    List<ServerAddress> addresses = new ArrayList<>();
    for (String connection : Arrays.asList(connections.split(","))) {
        String[] hostPort = parseHostAndPort(connection);
        String host = hostPort[0];
        int port = parsePort(hostPort[1]);
        addresses.add(new ServerAddress(host, port));
    }
    return addresses;
}
// ---- helper method(s) introduced by the refactoring ----
private static String[] parseHostAndPort(String connection) {
    return connection.split(":");
}

private static int parsePort(String portStr) {
    return StringUtils.isEmpty(portStr) ? DEFAULT_PORT : Integer.parseInt(portStr.trim());
}

