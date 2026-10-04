@Override
public void run() {
    active = true;
    while (active) {
        try {
            Message reply = consumer.receive(5000);
            if (reply != null) {
                processMessage(reply);
            }
        } catch (JMSException e1) {
            log.error("Error handling receive", e1);
        }
    }
    cleanup();
}
// ---- helper method(s) introduced by the refactoring ----
private void processMessage(Message reply) {
    String messageKey = getMessageKey(reply);
    if (messageKey != null) {
        synchronized (MessageAdmin.getAdmin()) {
            MessageAdmin.getAdmin().putReply(messageKey, reply);
        }
    }
}

private String getMessageKey(Message reply) {
    try {
        return useResMsgIdAsCorrelId ? reply.getJMSMessageID() : reply.getJMSCorrelationID();
    } catch (JMSException e) {
        log.error("Error retrieving message key", e);
        return null;
    }
}

private void cleanup() {
    Utils.close(consumer, log);
    Utils.close(session, log);
    Utils.close(conn, log);
}

