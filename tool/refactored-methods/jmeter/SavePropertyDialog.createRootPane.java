@Override
protected JRootPane createRootPane() {
    JRootPane rootPane = new JRootPane();
    Action escapeAction = createEscapeAction();
    InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    inputMap.put(KeyStrokes.ESC, escapeAction.getValue(Action.NAME));
    rootPane.getActionMap().put(escapeAction.getValue(Action.NAME), escapeAction);
    return rootPane;
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

