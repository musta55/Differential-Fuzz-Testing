private void initDialog() {
    this.getContentPane().setLayout(new BorderLayout());
    final int configCount = (SampleSaveConfiguration.SAVE_CONFIG_NAMES.size() / 3) + 1;
    log.debug("grid panel is {} by {}", 3, configCount);
    JPanel checkPanel = new JPanel(new GridLayout(configCount, 3));
    for (final String name : SampleSaveConfiguration.SAVE_CONFIG_NAMES) {
        try {
            JCheckBox check = new JCheckBox(JMeterUtils.getResString(RESOURCE_PREFIX + name), getSaveState(SampleSaveConfiguration.getterName(name)));
            check.addActionListener(this);
            // $NON-NLS-1$
            final String actionCommand = SampleSaveConfiguration.setterName(name);
            check.setActionCommand(actionCommand);
            if (!functors.containsKey(actionCommand)) {
                functors.put(actionCommand, new Functor(actionCommand));
            }
            checkPanel.add(check, BorderLayout.NORTH);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.warn("Problem creating save config dialog", e);
        }
    }
    getContentPane().add(checkPanel, BorderLayout.NORTH);
    // $NON-NLS-1$
    JButton exit = new JButton(JMeterUtils.getResString("done"));
    this.getContentPane().add(exit, BorderLayout.SOUTH);
    exit.addActionListener(e -> dispose());
}