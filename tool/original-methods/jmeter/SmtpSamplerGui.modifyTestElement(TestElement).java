/**
 * Modifies a given TestElement to mirror the data in the gui components
 * @param te TestElement for JMeter
 * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(org.apache.jmeter.testelement.TestElement)
 */
@Override
public void modifyTestElement(TestElement te) {
    te.clear();
    super.configureTestElement(te);
    te.setProperty(SmtpSampler.SERVER, smtpPanel.getServer());
    te.setProperty(SmtpSampler.SERVER_PORT, smtpPanel.getPort());
    // $NON-NLS-1$
    te.setProperty(SmtpSampler.SERVER_TIMEOUT, smtpPanel.getTimeout(), "");
    // $NON-NLS-1$
    te.setProperty(SmtpSampler.SERVER_CONNECTION_TIMEOUT, smtpPanel.getConnectionTimeout(), "");
    te.setProperty(SmtpSampler.MAIL_FROM, smtpPanel.getMailFrom());
    te.setProperty(SmtpSampler.MAIL_REPLYTO, smtpPanel.getMailReplyTo());
    te.setProperty(SmtpSampler.RECEIVER_TO, smtpPanel.getReceiverTo());
    te.setProperty(SmtpSampler.RECEIVER_CC, smtpPanel.getReceiverCC());
    te.setProperty(SmtpSampler.RECEIVER_BCC, smtpPanel.getReceiverBCC());
    te.setProperty(SmtpSampler.SUBJECT, smtpPanel.getSubject());
    te.setProperty(SmtpSampler.SUPPRESS_SUBJECT, Boolean.toString(smtpPanel.isSuppressSubject()));
    te.setProperty(SmtpSampler.INCLUDE_TIMESTAMP, Boolean.toString(smtpPanel.isIncludeTimestamp()));
    te.setProperty(SmtpSampler.MESSAGE, smtpPanel.getBody());
    te.setProperty(SmtpSampler.PLAIN_BODY, Boolean.toString(smtpPanel.isPlainBody()));
    te.setProperty(SmtpSampler.ATTACH_FILE, smtpPanel.getAttachments());
    SecuritySettingsPanel secPanel = smtpPanel.getSecuritySettingsPanel();
    secPanel.modifyTestElement(te);
    te.setProperty(SmtpSampler.USE_EML, smtpPanel.isUseEmlMessage());
    te.setProperty(SmtpSampler.EML_MESSAGE_TO_SEND, smtpPanel.getEmlMessage());
    te.setProperty(SmtpSampler.USE_AUTH, Boolean.toString(smtpPanel.isUseAuth()));
    te.setProperty(SmtpSampler.PASSWORD, smtpPanel.getPassword());
    te.setProperty(SmtpSampler.USERNAME, smtpPanel.getUsername());
    te.setProperty(SmtpSampler.MESSAGE_SIZE_STATS, Boolean.toString(smtpPanel.isMessageSizeStatistics()));
    te.setProperty(SmtpSampler.ENABLE_DEBUG, Boolean.toString(smtpPanel.isEnableDebug()));
    te.setProperty(smtpPanel.getHeaderFields());
}