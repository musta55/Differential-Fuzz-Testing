public static void main(String[] args) throws IOException {
    int port = UDP_PORT_DEFAULT;
    if (args.length > 1) {
        port = Integer.parseInt(args[1]);
    } else if (args.length == 0) {
        throw new RuntimeException("Usage: command [port]");
    }
    String command = args[0];
    System.out.println("Sending " + command + " request to port " + port);
    try (DatagramSocket socket = new DatagramSocket()) {
        byte[] buf = command.getBytes(StandardCharsets.US_ASCII);
        InetAddress address = InetAddress.getByName("localhost");
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
    }
}