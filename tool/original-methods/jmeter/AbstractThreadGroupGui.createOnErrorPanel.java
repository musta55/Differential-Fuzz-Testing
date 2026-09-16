private JPanel createOnErrorPanel() {
    JPanel panel = new JPanel(new MigLayout());
    panel.setBorder(BorderFactory.createTitledBorder(// $NON-NLS-1$
    JMeterUtils.getResString("sampler_on_error_action")));
    ButtonGroup group = new ButtonGroup();
    continueBox = new JRadioButton(// $NON-NLS-1$
    JMeterUtils.getResString("sampler_on_error_continue"));
    group.add(continueBox);
    panel.add(continueBox);
    startNextLoop = new JRadioButton(// $NON-NLS-1$
    JMeterUtils.getResString("sampler_on_error_start_next_loop"));
    group.add(startNextLoop);
    panel.add(startNextLoop);
    stopThreadBox = new JRadioButton(// $NON-NLS-1$
    JMeterUtils.getResString("sampler_on_error_stop_thread"));
    group.add(stopThreadBox);
    panel.add(stopThreadBox);
    stopTestBox = new JRadioButton(// $NON-NLS-1$
    JMeterUtils.getResString("sampler_on_error_stop_test"));
    group.add(stopTestBox);
    panel.add(stopTestBox);
    stopTestNowBox = new JRadioButton(// $NON-NLS-1$
    JMeterUtils.getResString("sampler_on_error_stop_test_now"));
    group.add(stopTestNowBox);
    panel.add(stopTestNowBox);
    return panel;
}