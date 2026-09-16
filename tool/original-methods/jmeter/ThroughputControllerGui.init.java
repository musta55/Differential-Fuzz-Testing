private void init() {
    // WARNING: called from ctor so must not be overridden (i.e. must be private or final)
    setLayout(new BorderLayout());
    setBorder(makeBorder());
    // TODO: add "insets 0", however, for now JComboBox consumes more space than its cell
    // for some reason.
    JPanel panel = new JPanel(new MigLayout("fillx, wrap 2", "[][fill,grow]"));
    DefaultComboBoxModel<String> styleModel = new DefaultComboBoxModel<>();
    styleModel.addElement(BYNUMBER_LABEL);
    styleModel.addElement(BYPERCENT_LABEL);
    styleBox = new JComboBox<>(styleModel);
    styleBox.addActionListener(evt -> {
        if (((String) styleBox.getSelectedItem()).equals(BYNUMBER_LABEL)) {
            style = ThroughputController.BYNUMBER;
        } else {
            style = ThroughputController.BYPERCENT;
        }
    });
    panel.add(JMeterUtils.labelFor(styleBox, "throughput_control_mode"));
    panel.add(styleBox);
    // TEXT FIELD
    throughput = new JTextField(15);
    panel.add(JMeterUtils.labelFor(throughput, "throughput_control_tplabel"));
    panel.add(throughput);
    // $NON-NLS-1$
    throughput.setText("1");
    // PERTHREAD FIELD
    perthread = new JCheckBox(PERTHREAD_LABEL, isPerThread);
    perthread.addItemListener(evt -> {
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            isPerThread = true;
        } else {
            isPerThread = false;
        }
    });
    panel.add(perthread, "span 2");
    add(makeTitlePanel(), BorderLayout.NORTH);
    add(panel, BorderLayout.CENTER);
}