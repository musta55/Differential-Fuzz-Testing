public int port() {
    return serverPort == null || serverPort.trim().isEmpty() ? 80 : Integer.parseInt(serverPort);
}