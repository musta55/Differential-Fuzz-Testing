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
    writeFieldIfTrue(writer, prop.saveBytes(), NODE_BYTES);
    writeFieldIfTrue(writer, prop.saveSentBytes(), NODE_SENT_BYTES);
    writeFieldIfTrue(writer, prop.saveUrl(), NODE_URL);
    writeFieldIfTrue(writer, prop.saveFileName(), NODE_FILENAME);
    writeFieldIfTrue(writer, prop.saveHostname(), NODE_HOSTNAME);
    writeFieldIfTrue(writer, prop.saveThreadCounts(), NODE_THREAD_COUNT);
    writeFieldIfTrue(writer, prop.saveSampleCount(), NODE_SAMPLE_COUNT);
    writeFieldIfTrue(writer, prop.saveIdleTime(), NODE_IDLE_TIME);
    writeFieldIfTrue(writer, prop.saveConnectTime(), NODE_CONNECT_TIME);
}
// ---- helper method(s) introduced by the refactoring ----
private static void writeFieldIfTrue(HierarchicalStreamWriter writer, boolean save, String node) {
    if (save) {
        writer.startNode(node);
        writer.setValue(TRUE);
        writer.endNode();
    }
}

private static boolean isExcludedField(String fieldName) {
    switch(fieldName) {
        case NODE_BYTES:
        case NODE_SENT_BYTES:
        case NODE_URL:
        case NODE_FILENAME:
        case NODE_HOSTNAME:
        case NODE_THREAD_COUNT:
        case NODE_SAMPLE_COUNT:
        case NODE_IDLE_TIME:
        case NODE_CONNECT_TIME:
        case NODE_DELIMITER:
        case NODE_PRINTMS:
            return true;
        default:
            return false;
    }
}

