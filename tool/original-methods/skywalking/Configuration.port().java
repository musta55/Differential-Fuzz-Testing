public int port() {
    return serverPort == null || serverPort.trim().length() == 0 ? 80 : Integer.parseInt(serverPort);
}