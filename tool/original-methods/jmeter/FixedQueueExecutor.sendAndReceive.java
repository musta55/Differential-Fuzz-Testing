/**
 * {@inheritDoc}
 */
@Override
public Message sendAndReceive(Message request, int deliveryMode, int priority, long expiration) throws JMSException {
    String id = request.getJMSCorrelationID();
    if (id == null && !useReqMsgIdAsCorrelId) {
        throw new IllegalArgumentException("Correlation id is null. Set the JMSCorrelationID header.");
    }
    final CountDownLatch countDownLatch = new CountDownLatch(1);
    final MessageAdmin admin = MessageAdmin.getAdmin();
    if (useReqMsgIdAsCorrelId) {
        // msgId not available until after send() is called
        // Note: there is only one admin object which is shared between all threads
        synchronized (admin) {
            // interlock with Receiver
            producer.send(request, deliveryMode, priority, expiration);
            id = request.getJMSMessageID();
            admin.putRequest(id, request, countDownLatch);
        }
    } else {
        admin.putRequest(id, request, countDownLatch);
        producer.send(request, deliveryMode, priority, expiration);
    }
    try {
        log.debug("{} will wait for reply {} started on {}", Thread.currentThread().getName(), id, System.currentTimeMillis());
        // This used to be request.wait(timeout_ms), where 0 means forever
        // However 0 means return immediately for the latch
        if (timeout == 0) {
            log.debug("Waiting infinitely for message");
            //
            countDownLatch.await();
        } else {
            if (!countDownLatch.await(timeout, TimeUnit.MILLISECONDS)) {
                log.debug("Timeout {} ms reached before getting a reply message", timeout);
            }
        }
        log.debug("{} done waiting for {} on {} ended on {}", Thread.currentThread().getName(), id, request, System.currentTimeMillis());
    } catch (InterruptedException e) {
        log.warn("Interrupt exception caught", e);
        Thread.currentThread().interrupt();
    }
    return admin.get(id);
}