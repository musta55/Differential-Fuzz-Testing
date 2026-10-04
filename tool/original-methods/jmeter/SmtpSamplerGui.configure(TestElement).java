/**
 * Copy the data from the test element to the GUI, method has to be implemented by interface
 * @param element Test-element to be used as data-input
 * @see org.apache.jmeter.gui.AbstractJMeterGuiComponent#configure(org.apache.jmeter.testelement.TestElement)
 */
@Override
public void configure(TestElement element) {
    if (smtpPanel == null) {
        smtpPanel = new SmtpPanel();
    }
    smtpPanel.setServer(element.getPropertyAsString(SmtpSampler.SERVER));
    smtpPanel.setPort(element.getPropertyAsString(SmtpSampler.SERVER_PORT));
    smtpPanel.setTimeout(element.getPropertyAsString(SmtpSampler.SERVER_TIMEOUT));
    smtpPanel.setConnectionTimeout(element.getPropertyAsString(SmtpSampler.SERVER_CONNECTION_TIMEOUT));
    smtpPanel.setMailFrom(element.getPropertyAsString(SmtpSampler.MAIL_FROM));
    smtpPanel.setMailReplyTo(element.getPropertyAsString(SmtpSampler.MAIL_REPLYTO));
    smtpPanel.setReceiverTo(element.getPropertyAsString(SmtpSampler.RECEIVER_TO));
    smtpPanel.setReceiverCC(element.getPropertyAsString(SmtpSampler.RECEIVER_CC));
    smtpPanel.setReceiverBCC(element.getPropertyAsString(SmtpSampler.RECEIVER_BCC));
    smtpPanel.setBody(element.getPropertyAsString(SmtpSampler.MESSAGE));
    smtpPanel.setPlainBody(element.getPropertyAsBoolean(SmtpSampler.PLAIN_BODY));
    smtpPanel.setSubject(element.getPropertyAsString(SmtpSampler.SUBJECT));
    smtpPanel.setSuppressSubject(element.getPropertyAsBoolean(SmtpSampler.SUPPRESS_SUBJECT));
    smtpPanel.setIncludeTimestamp(element.getPropertyAsBoolean(SmtpSampler.INCLUDE_TIMESTAMP));
    JMeterProperty headers = element.getProperty(SmtpSampler.HEADER_FIELDS);
    if (headers instanceof CollectionProperty) {
        // Might be NullProperty
        smtpPanel.setHeaderFields((CollectionProperty) headers);
    } else {
        smtpPanel.setHeaderFields(new CollectionProperty());
    }
    smtpPanel.setAttachments(element.getPropertyAsString(SmtpSampler.ATTACH_FILE));
    smtpPanel.setUseEmlMessage(element.getPropertyAsBoolean(SmtpSampler.USE_EML));
    smtpPanel.setEmlMessage(element.getPropertyAsString(SmtpSampler.EML_MESSAGE_TO_SEND));
    SecuritySettingsPanel secPanel = smtpPanel.getSecuritySettingsPanel();
    secPanel.configure(element);
    smtpPanel.setUseAuth(element.getPropertyAsBoolean(SmtpSampler.USE_AUTH));
    smtpPanel.setUsername(element.getPropertyAsString(SmtpSampler.USERNAME));
    smtpPanel.setPassword(element.getPropertyAsString(SmtpSampler.PASSWORD));
    smtpPanel.setMessageSizeStatistic(element.getPropertyAsBoolean(SmtpSampler.MESSAGE_SIZE_STATS));
    smtpPanel.setEnableDebug(element.getPropertyAsBoolean(SmtpSampler.ENABLE_DEBUG));
    super.configure(element);
}