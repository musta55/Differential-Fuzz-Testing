/**
 * {@inheritDoc}
 */
@Override
public void modifyTestElement(TestElement te) {
    te.clear();
    configureTestElement(te);
    MailReaderSampler mrs = (MailReaderSampler) te;
    mrs.setServerType(serverTypeBox.getText());
    mrs.setFolder(folderBox.getText());
    mrs.setServer(serverBox.getText());
    mrs.setPort(portBox.getText());
    mrs.setUserName(usernameBox.getText());
    mrs.setPassword(passwordBox.getText());
    if (allMessagesButton.isSelected()) {
        mrs.setNumMessages(MailReaderSampler.ALL_MESSAGES);
    } else {
        mrs.setNumMessages(someMessagesField.getText());
    }
    mrs.setHeaderOnly(headerOnlyBox.isSelected());
    mrs.setDeleteMessages(deleteBox.isSelected());
    mrs.setStoreMimeMessage(storeMimeMessageBox.isSelected());
    securitySettingsPanel.modifyTestElement(te);
}