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
private Action createEscapeAction() {
    return new AbstractAction("ESCAPE") {

        /**
         */
        private static final long serialVersionUID = 2208129319916921772L;

        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
        }
    };
}

