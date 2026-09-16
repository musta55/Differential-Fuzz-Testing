@Override
protected JRootPane createRootPane() {
    JRootPane rootPane = new JRootPane();
    Action escapeAction = new AbstractAction("ESCAPE") {

        /**
         */
        private static final long serialVersionUID = 2208129319916921772L;

        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
        }
    };
    InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    inputMap.put(KeyStrokes.ESC, escapeAction.getValue(Action.NAME));
    rootPane.getActionMap().put(escapeAction.getValue(Action.NAME), escapeAction);
    return rootPane;
}