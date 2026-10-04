/**
 * Modifies a given TestElement to mirror the data in the gui components
 * @param te TestElement for JMeter
 * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(org.apache.jmeter.testelement.TestElement)
 */
@Override
public void modifyTestElement(TestElement te) {
    te.clear();
    super.configureTestElement(te);
    setElementPropertiesFromSmtpPanel(te);
    setElementPropertiesFromSecuritySettingsPanel(te);
    setAdditionalElementProperties(te);
}
// ---- helper method(s) introduced by the refactoring ----
private void setSmtpPanelPropertiesFromElement(TestElement element) {
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
}

private void setSecuritySettingsFromElement(TestElement element) {
    SecuritySettingsPanel secPanel = smtpPanel.getSecuritySettingsPanel();
    secPanel.configure(element);
    smtpPanel.setUseAuth(element.getPropertyAsBoolean(SmtpSampler.USE_AUTH));
    smtpPanel.setUsername(element.getPropertyAsString(SmtpSampler.USERNAME));
    smtpPanel.setPassword(element.getPropertyAsString(SmtpSampler.PASSWORD));
}

private void setAdditionalPropertiesFromElement(TestElement element) {
    smtpPanel.setUseEmlMessage(element.getPropertyAsBoolean(SmtpSampler.USE_EML));
    smtpPanel.setEmlMessage(element.getPropertyAsString(SmtpSampler.EML_MESSAGE_TO_SEND));
    smtpPanel.setMessageSizeStatistic(element.getPropertyAsBoolean(SmtpSampler.MESSAGE_SIZE_STATS));
    smtpPanel.setEnableDebug(element.getPropertyAsBoolean(SmtpSampler.ENABLE_DEBUG));
}

private void setElementPropertiesFromSmtpPanel(TestElement te) {
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
}

private void setElementPropertiesFromSecuritySettingsPanel(TestElement te) {
    SecuritySettingsPanel secPanel = smtpPanel.getSecuritySettingsPanel();
    secPanel.modifyTestElement(te);
    te.setProperty(SmtpSampler.USE_AUTH, Boolean.toString(smtpPanel.isUseAuth()));
    te.setProperty(SmtpSampler.PASSWORD, smtpPanel.getPassword());
    te.setProperty(SmtpSampler.USERNAME, smtpPanel.getUsername());
}

private void setAdditionalElementProperties(TestElement te) {
    te.setProperty(SmtpSampler.USE_EML, smtpPanel.isUseEmlMessage());
    te.setProperty(SmtpSampler.EML_MESSAGE_TO_SEND, smtpPanel.getEmlMessage());
    te.setProperty(SmtpSampler.MESSAGE_SIZE_STATS, Boolean.toString(smtpPanel.isMessageSizeStatistics()));
    te.setProperty(SmtpSampler.ENABLE_DEBUG, Boolean.toString(smtpPanel.isEnableDebug()));
    te.setProperty(smtpPanel.getHeaderFields());
}

