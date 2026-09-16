/**
 * Implements JMeterGUIComponent.clearGui
 */
@Override
public void clearGui() {
    super.clearGui();
    resetComponents();
}
// ---- helper method(s) introduced by the refactoring ----
private void updateTestElement(ThroughputController tc) {
    tc.setStyle(style);
    tc.setPerThread(isPerThread);
    if (style == ThroughputController.BYNUMBER) {
        parseAndSetMaxThroughput(tc);
    } else {
        parseAndSetPercentThroughput(tc);
    }
}

private void parseAndSetMaxThroughput(ThroughputController tc) {
    try {
        tc.setMaxThroughput(Integer.parseInt(throughput.getText().trim()));
    } catch (NumberFormatException e) {
        // $NON-NLS-1$
        tc.setMaxThroughput(throughput.getText().trim().split("\\.")[0]);
    }
}

private void parseAndSetPercentThroughput(ThroughputController tc) {
    try {
        tc.setPercentThroughput(Float.parseFloat(throughput.getText().trim()));
    } catch (NumberFormatException e) {
        tc.setPercentThroughput(throughput.getText());
    }
}

private void resetComponents() {
    styleBox.setSelectedIndex(1);
    // $NON-NLS-1$
    throughput.setText("1");
    perthread.setSelected(false);
}

private void updateGuiComponents(ThroughputController el) {
    if (el.getStyle() == ThroughputController.BYNUMBER) {
        styleBox.getModel().setSelectedItem(BYNUMBER_LABEL);
        throughput.setText(el.getMaxThroughput());
    } else {
        styleBox.setSelectedItem(BYPERCENT_LABEL);
        throughput.setText(el.getPercentThroughput());
    }
    perthread.setSelected(el.isPerThread());
}

private void addComponents() {
    JPanel panel = new JPanel(new MigLayout("fillx, wrap 2", "[][fill,grow]"));
    addStyleBox(panel);
    addThroughputField(panel);
    addPerThreadCheckBox(panel);
    addPanelsToMainPanel(panel);
}

private void addStyleBox(JPanel panel) {
    DefaultComboBoxModel<String> styleModel = new DefaultComboBoxModel<>();
    styleModel.addElement(BYNUMBER_LABEL);
    styleModel.addElement(BYPERCENT_LABEL);
    styleBox = new JComboBox<>(styleModel);
    styleBox.addActionListener(evt -> updateStyle());
    panel.add(JMeterUtils.labelFor(styleBox, "throughput_control_mode"));
    panel.add(styleBox);
}

private void updateStyle() {
    style = ((String) styleBox.getSelectedItem()).equals(BYNUMBER_LABEL) ? ThroughputController.BYNUMBER : ThroughputController.BYPERCENT;
}

private void addThroughputField(JPanel panel) {
    throughput = new JTextField(15);
    // $NON-NLS-1$
    throughput.setText("1");
    panel.add(JMeterUtils.labelFor(throughput, "throughput_control_tplabel"));
    panel.add(throughput);
}

private void addPerThreadCheckBox(JPanel panel) {
    perthread = new JCheckBox(PERTHREAD_LABEL, isPerThread);
    perthread.addItemListener(evt -> updatePerThreadStatus(evt));
    panel.add(perthread, "span 2");
}

private void updatePerThreadStatus(ItemEvent evt) {
    isPerThread = evt.getStateChange() == ItemEvent.SELECTED;
}

private void addPanelsToMainPanel(JPanel panel) {
    add(makeTitlePanel(), BorderLayout.NORTH);
    add(panel, BorderLayout.CENTER);
}

