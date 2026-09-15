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
        EventsAgent.EventInfo eventInfo = new EventsAgent.EventInfo();
        eventInfo.id = event.getId();
        eventInfo.timestamp = event.getTimestamp();
        eventInfo.type = event.getType();
        eventInfo.data = data;
        eventInfo.data.remove("id");
        wsClient.publish(pubSubTopic, eventInfo);
    }
}