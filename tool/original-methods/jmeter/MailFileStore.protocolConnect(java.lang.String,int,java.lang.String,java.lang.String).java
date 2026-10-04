@Override
protected boolean protocolConnect(String host, int port, String user, String password) throws MessagingException {
    File base = new File(host);
    if (base.isDirectory() || base.isFile()) {
        return true;
    }
    throw new MessagingException("Host must be a valid directory or file");
}