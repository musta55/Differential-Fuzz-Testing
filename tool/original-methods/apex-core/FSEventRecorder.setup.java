public void setup() {
    try {
        streamCodec = new JsonStreamCodec<>();
        storage = new FSPartFileCollection();
        storage.setBasePath(basePath);
        storage.setup();
        storage.writeMetaData((VERSION + "\n").getBytes());
        if (wsClient != null) {
            try {
                setupWsClient();
            } catch (ExecutionException | IOException | InterruptedException | TimeoutException ex) {
                LOG.error("Cannot connect to gateway at {}", pubSubUrl);
            }
        }
        eventRecorderThread.start();
    } catch (Exception ex) {
        throw Throwables.propagate(ex);
    }
}