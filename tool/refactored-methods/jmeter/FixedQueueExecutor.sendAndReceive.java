/**
 * {@inheritDoc}
 */
@Override
public Message sendAndReceive(Message request, int deliveryMode, int priority, long expiration) throws JMSException {
    String correlationId = setCorrelationId(request);
    final CountDownLatch countDownLatch = new CountDownLatch(1);
    final MessageAdmin admin = MessageAdmin.getAdmin();
    if (useReqMsgIdAsCorrelId) {
        synchronized (admin) {
            sendMessage(request, deliveryMode, priority, expiration);
            correlationId = request.getJMSMessageID();
            admin.putRequest(correlationId, request, countDownLatch);
        }
    } else {
        admin.putRequest(correlationId, request, countDownLatch);
        sendMessage(request, deliveryMode, priority, expiration);
    }
    waitForReply(countDownLatch, correlationId, request);
    return admin.get(correlationId);
}
// ---- helper method(s) introduced by the refactoring ----
private String setCorrelationId(Message request) throws JMSException {
    String id = request.getJMSCorrelationID();
    if (id == null && !useReqMsgIdAsCorrelId) {
        throw new IllegalArgumentException("Correlation id is null. Set the JMSCorrelationID header.");
    }
    return id;
}

private void sendMessage(Message request, int deliveryMode, int priority, long expiration) throws JMSException {
    producer.send(request, deliveryMode, priority, expiration);
}

private void waitForReply(CountDownLatch countDownLatch, String correlationId, Message request) {
    try {
        log.debug("{} will wait for reply {} started on {}", Thread.currentThread().getName(), correlationId, System.currentTimeMillis());
        if (timeout == 0) {
            log.debug("Waiting infinitely for message");
            countDownLatch.await();
        } else {
            if (!countDownLatch.await(timeout, TimeUnit.MILLISECONDS)) {
                log.debug("Timeout {} ms reached before getting a reply message", timeout);
            }
        }
        log.debug("{} done waiting for {} on {} ended on {}", Thread.currentThread().getName(), correlationId, request, System.currentTimeMillis());
    } catch (InterruptedException e) {
        log.warn("Interrupt exception caught", e);
        Thread.currentThread().interrupt();
    }
}

