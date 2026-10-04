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
    buttonMap.put(OnErrorTestElement.ON_ERROR_CONTINUE, continueBox);
    buttonMap.put(OnErrorTestElement.ON_ERROR_BREAK_CURRENT_LOOP, breakLoopBox);
    buttonMap.put(OnErrorTestElement.ON_ERROR_START_NEXT_THREAD_LOOP, startNextThreadLoopBox);
    buttonMap.put(OnErrorTestElement.ON_ERROR_START_NEXT_ITERATION_OF_CURRENT_LOOP, startNextIterationOfCurrentLoopBox);
    buttonMap.put(OnErrorTestElement.ON_ERROR_STOPTEST, stopTestBox);
    buttonMap.put(OnErrorTestElement.ON_ERROR_STOPTEST_NOW, stopTestNowBox);
    buttonMap.put(OnErrorTestElement.ON_ERROR_STOPTHREAD, stopThrdBox);
    return panel;
}