/**
 * Closes the <code>TimeoutEnabledQueueRequestor</code> and its session.
 *
 * <P>
 * Since a provider may allocate some resources on behalf of a
 * <code>TimeoutEnabledQueueRequestor</code> outside the Java virtual
 * machine, clients should close them when they are not needed. Relying on
 * garbage collection to eventually reclaim these resources may not be
 * timely enough.
 *
 * <P>
 * This method closes the <code>Session</code> object passed to the
 * <code>TimeoutEnabledQueueRequestor</code> constructor.
 *
 * @exception JMSException
 *                if the JMS provider fails to close the
 *                <code>TimeoutEnabledQueueRequestor</code> due to some
 *                internal error.
 */
public void close() throws JMSException {
    String queueName = tempQueue.getQueueName();
    try {
        sender.close();
    } catch (Exception ex) {
        logger.error("Error closing sender", ex);
    }
    try {
        receiver.close();
    } catch (Exception ex) {
        logger.error("Error closing receiver", ex);
    }
    try {
        tempQueue.delete();
    } catch (Exception ex) {
        logger.error("Error deleting tempQueue {}", queueName, ex);
    }
}