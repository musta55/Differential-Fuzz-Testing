public void setup() {
    try {
        initializeStreamCodec();
        initializeStorage();
        setupWebSocketIfAvailable();
        startEventRecorderThread();
    } catch (Exception ex) {
        throw Throwables.propagate(ex);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void initializeStreamCodec() {
    streamCodec = new JsonStreamCodec<>();
}

private void initializeStorage() throws IOException {
    storage = new FSPartFileCollection();
    storage.setBasePath(basePath);
    storage.setup();
    storage.writeMetaData((VERSION + "\n").getBytes());
}

private void setupWebSocketIfAvailable() throws ExecutionException, IOException, InterruptedException, TimeoutException {
    if (wsClient != null) {
        setupWsClient();
    }
}

private void startEventRecorderThread() {
    eventRecorderThread.start();
}

private EventsAgent.EventInfo createEventInfo(StramEvent event, Map<String, String> data) {
    EventsAgent.EventInfo eventInfo = new EventsAgent.EventInfo();
    eventInfo.id = event.getId();
    eventInfo.timestamp = event.getTimestamp();
    eventInfo.type = event.getType();
    eventInfo.data = data;
    eventInfo.data.remove("id");
    return eventInfo;
}

