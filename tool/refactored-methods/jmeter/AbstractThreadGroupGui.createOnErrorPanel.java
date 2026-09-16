private JPanel createOnErrorPanel() {
    JPanel panel = new JPanel(new MigLayout());
    panel.setBorder(BorderFactory.createTitledBorder(// $NON-NLS-1$
    JMeterUtils.getResString("sampler_on_error_action")));
    ButtonGroup group = new ButtonGroup();
    continueBox = createRadioButton(group, "sampler_on_error_continue");
    panel.add(continueBox);
    startNextLoop = createRadioButton(group, "sampler_on_error_start_next_loop");
    panel.add(startNextLoop);
    stopThreadBox = createRadioButton(group, "sampler_on_error_stop_thread");
    panel.add(stopThreadBox);
    stopTestBox = createRadioButton(group, "sampler_on_error_stop_test");
    panel.add(stopTestBox);
    stopTestNowBox = createRadioButton(group, "sampler_on_error_stop_test_now");
    panel.add(stopTestNowBox);
    return panel;
}
// ---- helper method(s) introduced by the refactoring ----
private static JRadioButton createRadioButton(ButtonGroup group, String key) {
    JRadioButton radioButton = new JRadioButton(JMeterUtils.getResString(key));
    group.add(radioButton);
    return radioButton;
}

