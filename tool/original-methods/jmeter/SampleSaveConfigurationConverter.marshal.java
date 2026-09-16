/**
 * {@inheritDoc}
 */
@Override
public void marshal(Object obj, HierarchicalStreamWriter writer, MarshallingContext context) {
    // Save most things
    super.marshal(obj, writer, context);
    SampleSaveConfiguration prop = (SampleSaveConfiguration) obj;
    // Save the new fields - but only if they are true
    // This list MUST agree with the list in MyWrapper#shouldSerializeMember()
    createNode(writer, prop.saveBytes(), NODE_BYTES);
    createNode(writer, prop.saveSentBytes(), NODE_SENT_BYTES);
    createNode(writer, prop.saveUrl(), NODE_URL);
    createNode(writer, prop.saveFileName(), NODE_FILENAME);
    createNode(writer, prop.saveHostname(), NODE_HOSTNAME);
    createNode(writer, prop.saveThreadCounts(), NODE_THREAD_COUNT);
    createNode(writer, prop.saveSampleCount(), NODE_SAMPLE_COUNT);
    createNode(writer, prop.saveIdleTime(), NODE_IDLE_TIME);
    createNode(writer, prop.saveConnectTime(), NODE_CONNECT_TIME);
}