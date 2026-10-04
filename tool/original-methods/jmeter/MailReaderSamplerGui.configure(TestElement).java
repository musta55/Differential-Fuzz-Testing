/**
 * {@inheritDoc}
 */
@Override
public void configure(TestElement element) {
    MailReaderSampler mrs = (MailReaderSampler) element;
    serverTypeBox.setText(mrs.getServerType());
    folderBox.setText(mrs.getFolder());
    serverBox.setText(mrs.getServer());
    portBox.setText(mrs.getPort());
    usernameBox.setText(mrs.getUserName());
    passwordBox.setText(mrs.getPassword());
    if (mrs.getNumMessages() == MailReaderSampler.ALL_MESSAGES) {
        allMessagesButton.setSelected(true);
        // $NON-NLS-1$
        someMessagesField.setText("0");
    } else {
        someMessagesButton.setSelected(true);
        someMessagesField.setText(mrs.getNumMessagesString());
    }
    headerOnlyBox.setSelected(mrs.getHeaderOnly());
    deleteBox.setSelected(mrs.getDeleteMessages());
    storeMimeMessageBox.setSelected(mrs.isStoreMimeMessage());
    securitySettingsPanel.configure(element);
    super.configure(element);
}