private void init() {
    setLayout(new BorderLayout());
    setBorder(makeBorder());
    JPanel settingsPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = getConstraints();
    serverTypeBox = new JTextField(20);
    serverTypeBox.addActionListener(this);
    serverTypeBox.addFocusListener(this);
    addField(settingsPanel, serverTypeLabel, serverTypeBox, gbc);
    serverBox = new JTextField(20);
    addField(settingsPanel, serverLabel, serverBox, gbc);
    portBox = new JTextField(20);
    addField(settingsPanel, portLabel, portBox, gbc);
    usernameBox = new JTextField(20);
    addField(settingsPanel, accountLabel, usernameBox, gbc);
    passwordBox = new JPasswordField(20);
    addField(settingsPanel, passwordLabel, passwordBox, gbc);
    folderLabel = new JLabel(folderLabelStr);
    folderBox = new JTextField(INBOX, 20);
    addField(settingsPanel, folderLabel, folderBox, gbc);
    HorizontalPanel numMessagesPanel = new HorizontalPanel();
    numMessagesPanel.add(new JLabel(numMessagesLabel));
    ButtonGroup nmbg = new ButtonGroup();
    allMessagesButton = new JRadioButton(allMessagesLabel);
    allMessagesButton.addChangeListener(e -> someMessagesField.setEnabled(!allMessagesButton.isSelected()));
    someMessagesButton = new JRadioButton();
    someMessagesButton.addChangeListener(e -> someMessagesField.setEnabled(someMessagesButton.isSelected()));
    nmbg.add(allMessagesButton);
    nmbg.add(someMessagesButton);
    someMessagesField = new JTextField(5);
    allMessagesButton.setSelected(true);
    numMessagesPanel.add(allMessagesButton);
    numMessagesPanel.add(someMessagesButton);
    numMessagesPanel.add(someMessagesField);
    headerOnlyBox = new JCheckBox(headerOnlyLabel);
    deleteBox = new JCheckBox(deleteLabel);
    storeMimeMessageBox = new JCheckBox(storeMime);
    securitySettingsPanel = new SecuritySettingsPanel();
    JPanel settings = new VerticalPanel();
    settings.add(Box.createVerticalStrut(5));
    settings.add(settingsPanel);
    settings.add(numMessagesPanel);
    settings.add(headerOnlyBox);
    settings.add(deleteBox);
    settings.add(storeMimeMessageBox);
    settings.add(securitySettingsPanel);
    add(makeTitlePanel(), BorderLayout.NORTH);
    add(settings, BorderLayout.CENTER);
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

