private JPanel createOnErrorPanel() {
    JPanel panel = new JPanel(new GridLayout(4, 2));
    //$NON-NLS-1$
    panel.setBorder(BorderFactory.createTitledBorder(JMeterUtils.getResString("sampler_on_error_action")));
    ButtonGroup group = new ButtonGroup();
    //$NON-NLS-1$
    continueBox = addRadioButton("sampler_on_error_continue", group, panel);
    //$NON-NLS-1$
    breakLoopBox = addRadioButton("sampler_on_error_break_loop", group, panel);
    //$NON-NLS-1$
    startNextThreadLoopBox = addRadioButton("sampler_on_error_start_next_loop", group, panel);
    //$NON-NLS-1$
    startNextIterationOfCurrentLoopBox = addRadioButton("sampler_on_error_start_next_iteration_current_loop", group, panel);
    //$NON-NLS-1$
    stopTestBox = addRadioButton("sampler_on_error_stop_test", group, panel);
    //$NON-NLS-1$
    stopTestNowBox = addRadioButton("sampler_on_error_stop_test_now", group, panel);
    //$NON-NLS-1$
    stopThrdBox = addRadioButton("sampler_on_error_stop_thread", group, panel);
    continueBox.setSelected(true);
    return panel;
}