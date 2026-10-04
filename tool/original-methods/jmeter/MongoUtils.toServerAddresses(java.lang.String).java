public static List<ServerAddress> toServerAddresses(String connections) throws UnknownHostException {
    List<ServerAddress> addresses = new ArrayList<>();
    for (String connection : Arrays.asList(connections.split(","))) {
        int port = DEFAULT_PORT;
        String[] hostPort = connection.split(":");
        if (hostPort.length > 1 && !StringUtils.isEmpty(hostPort[1])) {
            port = Integer.parseInt(hostPort[1].trim());
        }
        addresses.add(new ServerAddress(hostPort[0], port));
    }
    return addresses;
}