@Override
public void modifyTestElement(TestElement te) {
    te.clear();
    configureTestElement(te);
    MailReaderSampler mrs = (MailReaderSampler) te;
    modifyServerType(mrs);
    modifyFolder(mrs);
    modifyServerDetails(mrs);
    modifyUserCredentials(mrs);
    modifyMessageOptions(mrs);
    modifySecuritySettings(te);
}
// ---- helper method(s) introduced by the refactoring ----
private void configureServerType(MailReaderSampler mrs) {
    serverTypeBox.setText(mrs.getServerType());
}

private void configureFolder(MailReaderSampler mrs) {
    folderBox.setText(mrs.getFolder());
}

private void configureServerDetails(MailReaderSampler mrs) {
    serverBox.setText(mrs.getServer());
    portBox.setText(mrs.getPort());
}

private void configureUserCredentials(MailReaderSampler mrs) {
    usernameBox.setText(mrs.getUserName());
    passwordBox.setText(mrs.getPassword());
}

private void configureMessageOptions(MailReaderSampler mrs) {
    if (mrs.getNumMessages() == MailReaderSampler.ALL_MESSAGES) {
        allMessagesButton.setSelected(true);
        someMessagesField.setText("0");
    } else {
        someMessagesButton.setSelected(true);
        someMessagesField.setText(mrs.getNumMessagesString());
    }
    headerOnlyBox.setSelected(mrs.getHeaderOnly());
    deleteBox.setSelected(mrs.getDeleteMessages());
    storeMimeMessageBox.setSelected(mrs.isStoreMimeMessage());
}

private void configureSecuritySettings(TestElement element) {
    securitySettingsPanel.configure(element);
}

private void modifyServerType(MailReaderSampler mrs) {
    mrs.setServerType(serverTypeBox.getText());
}

private void modifyFolder(MailReaderSampler mrs) {
    mrs.setFolder(folderBox.getText());
}

private void modifyServerDetails(MailReaderSampler mrs) {
    mrs.setServer(serverBox.getText());
    mrs.setPort(portBox.getText());
}

private void modifyUserCredentials(MailReaderSampler mrs) {
    mrs.setUserName(usernameBox.getText());
    mrs.setPassword(passwordBox.getText());
}

private void modifyMessageOptions(MailReaderSampler mrs) {
    if (allMessagesButton.isSelected()) {
        mrs.setNumMessages(MailReaderSampler.ALL_MESSAGES);
    } else {
        mrs.setNumMessages(someMessagesField.getText());
    }
    mrs.setHeaderOnly(headerOnlyBox.isSelected());
    mrs.setDeleteMessages(deleteBox.isSelected());
    mrs.setStoreMimeMessage(storeMimeMessageBox.isSelected());
}

private void modifySecuritySettings(TestElement te) {
    securitySettingsPanel.modifyTestElement(te);
}

