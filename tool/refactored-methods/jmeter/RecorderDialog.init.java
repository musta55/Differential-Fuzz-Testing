private void init() {
    // WARNING: called from ctor so must not be overridden (i.e. must be private or final)
    this.getContentPane().setLayout(new BorderLayout(10, 10));
    setupHttpSampleNamingMode();
    setupPrefixHTTPSampleName();
    setupProxyPauseHTTPSample();
    setupSampleNameFormat();
    setupCounterPanel();
    String iconSize = JMeterUtils.getPropDefault(JMeterToolBar.TOOLBAR_ICON_SIZE, JMeterToolBar.DEFAULT_TOOLBAR_ICON_SIZE);
    stop = recorderGui.createStopButton(iconSize);
    stop.addActionListener(this);
    GridLayout gridLayout = new GridLayout(1, 1);
    JPanel panelStop = new JPanel(gridLayout);
    panelStop.add(stop);
    this.getContentPane().add(panelStop, BorderLayout.WEST);
    this.pack();
    this.setLocation(5, 10);
    prefixHTTPSampleName.requestFocusInWindow();
}
// ---- helper method(s) introduced by the refactoring ----
private void setupHttpSampleNamingMode() {
    DefaultComboBoxModel<String> choice = new DefaultComboBoxModel<>();
    // $NON-NLS-1$
    choice.addElement(JMeterUtils.getResString("sample_name_prefix"));
    // $NON-NLS-1$
    choice.addElement(JMeterUtils.getResString("sample_name_transaction"));
    // $NON-NLS-1$
    choice.addElement(JMeterUtils.getResString("sample_name_suffix"));
    // $NON-NLS-1$
    choice.addElement(JMeterUtils.getResString("sample_name_formatter"));
    httpSampleNamingMode = new JComboBox<>(choice);
    httpSampleNamingMode.setName(ProxyControlGui.HTTP_SAMPLER_NAMING_MODE);
    httpSampleNamingMode.addItemListener(this);
}

private void setupPrefixHTTPSampleName() {
    prefixHTTPSampleName = new JTextField(20);
    prefixHTTPSampleName.addKeyListener(this);
    prefixHTTPSampleName.setName(ProxyControlGui.PREFIX_HTTP_SAMPLER_NAME);
}

private void setupProxyPauseHTTPSample() {
    proxyPauseHTTPSample = new JTextField(10);
    proxyPauseHTTPSample.addKeyListener(this);
    proxyPauseHTTPSample.setName(ProxyControlGui.PROXY_PAUSE_HTTP_SAMPLER);
    proxyPauseHTTPSample.setActionCommand(ProxyControlGui.ENABLE_RESTART);
    // $NON-NLS-1$
    JLabel labelProxyPause = new JLabel(JMeterUtils.getResString("proxy_pause_http_sampler"));
    labelProxyPause.setLabelFor(proxyPauseHTTPSample);
}

private void setupSampleNameFormat() {
    sampleNameFormat = new JTextField(20);
    sampleNameFormat.addKeyListener(this);
    sampleNameFormat.setName(ProxyControlGui.HTTP_SAMPLER_NAME_FORMAT);
    sampleNameFormat.setEnabled(httpSampleNamingMode.getSelectedIndex() == 3);
    sampleNameFormat.setToolTipText(JMeterUtils.getResString("sample_naming_format_help"));
}

private static void addCounterPanel(JPanel panel) {
    JLabel labelSetCounter = new JLabel(JMeterUtils.getResString("sample_creator_counter_value"));
    JTextField counterValue = new JTextField(10);
    labelSetCounter.setLabelFor(counterValue);
    JButton buttonSetCounter = new JButton(JMeterUtils.getResString("sample_creator_set_counter"));
    buttonSetCounter.addActionListener(e -> Proxy.setCounter(Integer.parseInt(counterValue.getText())));
    panel.add(labelSetCounter);
    panel.add(counterValue);
    panel.add(buttonSetCounter);
}

private void setupCounterPanel() {
    JPanel panel = new JPanel(new MigLayout("fillx, wrap 3"));
    panel.setBorder(BorderFactory.createTitledBorder(// $NON-NLS-1$
    JMeterUtils.getResString("proxy_sampler_settings")));
    JLabel labelTransactionName = new JLabel(JMeterUtils.getResString("sample_name_transaction"));
    labelTransactionName.setLabelFor(prefixHTTPSampleName);
    panel.add(labelTransactionName);
    panel.add(prefixHTTPSampleName, "span");
    JLabel labelNamingScheme = new JLabel(JMeterUtils.getResString("sample_naming_scheme"));
    labelNamingScheme.setLabelFor(httpSampleNamingMode);
    panel.add(labelNamingScheme, "split 2");
    panel.add(httpSampleNamingMode);
    panel.add(sampleNameFormat, "span");
    addCounterPanel(panel);
    panel.add(new JLabel(JMeterUtils.getResString("proxy_pause_http_sampler")), "span");
    panel.add(proxyPauseHTTPSample, "span");
    this.getContentPane().add(panel, BorderLayout.CENTER);
}

private void validateField(KeyEvent e) {
    String fieldName = e.getComponent().getName();
    if (fieldName.equals(ProxyControlGui.PREFIX_HTTP_SAMPLER_NAME)) {
        recorderGui.setPrefixHTTPSampleName(prefixHTTPSampleName.getText());
    } else if (fieldName.equals(ProxyControlGui.HTTP_SAMPLER_NAME_FORMAT)) {
        recorderGui.setSampleNameFormat(sampleNameFormat.getText());
    } else if (fieldName.equals(ProxyControlGui.PROXY_PAUSE_HTTP_SAMPLER)) {
        validateProxyPauseHTTPSample();
    }
}

private void validateProxyPauseHTTPSample() {
    try {
        Long.parseLong(proxyPauseHTTPSample.getText());
    } catch (NumberFormatException nfe) {
        handleInvalidInput();
    }
    recorderGui.setProxyPauseHTTPSample(proxyPauseHTTPSample.getText());
    recorderGui.enableRestart();
}

private void handleInvalidInput() {
    int length = proxyPauseHTTPSample.getText().length();
    if (length > 0) {
        // $NON-NLS-1$
        JOptionPane.// $NON-NLS-1$
        showMessageDialog(// $NON-NLS-1$
        this, // $NON-NLS-1$
        JMeterUtils.getResString("proxy_settings_pause_error_digits"), // $NON-NLS-1$
        JMeterUtils.getResString("proxy_settings_pause_error_invalid_data"), JOptionPane.WARNING_MESSAGE);
        // Drop the last character:
        proxyPauseHTTPSample.setText(proxyPauseHTTPSample.getText().substring(0, length - 1));
    }
}

