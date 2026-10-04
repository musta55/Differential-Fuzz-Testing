/**
 * Constructor
 * @param factory
 * @param receiveQueue Receive Queue
 * @param principal Username
 * @param credentials Password
 * @param useResMsgIdAsCorrelId
 * @param jmsSelector JMS Selector
 * @throws JMSException
 */
private Receiver(ConnectionFactory factory, Destination receiveQueue, String principal, String credentials, boolean useResMsgIdAsCorrelId, String jmsSelector) throws JMSException {
    if (null != principal && null != credentials) {
        log.info("creating receiver WITH authorisation credentials. UseResMsgId={}", useResMsgIdAsCorrelId);
        conn = factory.createConnection(principal, credentials);
    } else {
        log.info("creating receiver without authorisation credentials. UseResMsgId={}", useResMsgIdAsCorrelId);
        conn = factory.createConnection();
    }
    session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
    log.debug("Receiver - ctor. Creating consumer with JMS Selector:{}", jmsSelector);
    if (StringUtils.isEmpty(jmsSelector)) {
        consumer = session.createConsumer(receiveQueue);
    } else {
        consumer = session.createConsumer(receiveQueue, jmsSelector);
    }
    this.useResMsgIdAsCorrelId = useResMsgIdAsCorrelId;
    log.debug("Receiver - ctor. Starting connection now");
    conn.start();
    log.info("Receiver - ctor. Connection to messaging system established");
}