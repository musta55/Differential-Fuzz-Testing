public void writeEvent(StramEvent event) throws Exception {
    LOG.debug("Writing event {} to the storage", event.getType());
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    bos.write((event.getTimestamp() + ":").getBytes());
    bos.write((event.getType() + ":").getBytes());
    @SuppressWarnings("unchecked")
    Map<String, String> data = BeanUtils.describe(event);
    data.remove("timestamp");
    data.remove("class");
    data.remove("type");
    Slice f = streamCodec.toByteArray(data);
    bos.write(f.buffer, f.offset, f.length);
    bos.write("\n".getBytes());
    storage.writeDataItem(bos.toByteArray(), true);
    if (numSubscribers > 0) {
        LOG.debug("Publishing event {} through websocket to gateway", event.getType());
        EventsAgent.EventInfo eventInfo = createEventInfo(event, data);
        wsClient.publish(pubSubTopic, eventInfo);
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

