/*
     * Helper method to set up the GUI screen
     */
private void init() {
    // WARNING: called from ctor so must not be overridden (i.e. must be private or final)
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
    allMessagesButton.addChangeListener(e -> {
        if (allMessagesButton.isSelected()) {
            someMessagesField.setEnabled(false);
        }
    });
    someMessagesButton = new JRadioButton();
    someMessagesButton.addChangeListener(e -> {
        if (someMessagesButton.isSelected()) {
            someMessagesField.setEnabled(true);
        }
    });
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