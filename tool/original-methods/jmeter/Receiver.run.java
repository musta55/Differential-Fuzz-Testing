@Override
public void run() {
    active = true;
    Message reply;
    while (active) {
        reply = null;
        try {
            reply = consumer.receive(5000);
            if (reply != null) {
                String messageKey;
                final MessageAdmin admin = MessageAdmin.getAdmin();
                if (useResMsgIdAsCorrelId) {
                    messageKey = reply.getJMSMessageID();
                    synchronized (admin) {
                        // synchronize with FixedQueueExecutor
                        admin.putReply(messageKey, reply);
                    }
                } else {
                    messageKey = reply.getJMSCorrelationID();
                    if (messageKey == null) {
                        // JMSMessageID cannot be null
                        log.warn("Received message with correlation id null. Discarding message ...");
                    } else {
                        synchronized (admin) {
                            admin.putReply(messageKey, reply);
                        }
                    }
                }
            }
        } catch (JMSException e1) {
            log.error("Error handling receive", e1);
        }
    }
    Utils.close(consumer, log);
    Utils.close(session, log);
    Utils.close(conn, log);
}