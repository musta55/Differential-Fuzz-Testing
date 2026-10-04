private void initDialog() {
    this.getContentPane().setLayout(new BorderLayout());
    final int configCount = (SampleSaveConfiguration.SAVE_CONFIG_NAMES.size() / 3) + 1;
    log.debug("grid panel is {} by {}", 3, configCount);
    JPanel checkPanel = new JPanel(new GridLayout(configCount, 3));
    addCheckboxes(checkPanel);
    getContentPane().add(checkPanel, BorderLayout.NORTH);
    addExitButton();
}
// ---- helper method(s) introduced by the refactoring ----
private void addCheckboxes(JPanel checkPanel) {
    for (final String name : SampleSaveConfiguration.SAVE_CONFIG_NAMES) {
        try {
            JCheckBox check = createCheckbox(name);
            checkPanel.add(check, BorderLayout.NORTH);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.warn("Problem creating save config dialog", e);
        }
    }
}

private JCheckBox createCheckbox(String name) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    JCheckBox check = new JCheckBox(JMeterUtils.getResString(RESOURCE_PREFIX + name), getSaveState(SampleSaveConfiguration.getterName(name)));
    check.addActionListener(this);
    String actionCommand = SampleSaveConfiguration.setterName(name);
    check.setActionCommand(actionCommand);
    if (!functors.containsKey(actionCommand)) {
        functors.put(actionCommand, new Functor(actionCommand));
    }
    return check;
}

private void addExitButton() {
    // $NON-NLS-1$
    JButton exit = new JButton(JMeterUtils.getResString("done"));
    this.getContentPane().add(exit, BorderLayout.SOUTH);
    exit.addActionListener(e -> dispose());
}

private Action createEscapeAction() {
    return new AbstractAction("ESCAPE") {

        private static final long serialVersionUID = 2208129319916921772L;

        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
        }
    };
}

