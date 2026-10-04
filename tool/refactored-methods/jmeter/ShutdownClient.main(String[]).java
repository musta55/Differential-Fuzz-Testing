public static void main(String[] args) throws IOException {
    CommandArgs commandArgs = parseCommandArgs(args);
    sendCommand(commandArgs.command, commandArgs.port);
}
// ---- helper method(s) introduced by the refactoring ----
private static CommandArgs parseCommandArgs(String[] args) {
    int port = UDP_PORT_DEFAULT;
    if (args.length > 1) {
        port = Integer.parseInt(args[1]);
    } else if (args.length == 0) {
        throw new RuntimeException("Usage: command [port]");
    }
    String command = args[0];
    return new CommandArgs(command, port);
}

private static void sendCommand(String command, int port) throws IOException {
    System.out.println("Sending " + command + " request to port " + port);
    try (DatagramSocket socket = new DatagramSocket()) {
        byte[] buf = command.getBytes(StandardCharsets.US_ASCII);
        InetAddress address = InetAddress.getByName("localhost");
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
    }
}

