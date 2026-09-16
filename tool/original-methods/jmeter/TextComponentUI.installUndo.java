/**
 * Installs an undo manager and keyboard shortcuts to a text component
 * @param component JTextField or JTextArea
 */
@API(since = "5.3", status = API.Status.INTERNAL)
public void installUndo(JTextComponent component) {
    // JMeter reuses Swing JComponents, so when user switches to another component,
    // JComponent#name is updated. However, we don't want user to be able to "undo" that
    // So when tree selection is changed, we increase undoEpoch. That enables
    // UndoManagers to treat that as "end of undo history"
    UndoManager manager = new DefaultUndoManager(undoEpoch);
    manager.setLimit(200);
    component.addPropertyChangeListener("document", new AddUndoableEditListenerPropertyChangeListener(manager));
    component.getActionMap().put("undo", new AbstractAction() {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (manager.canUndo()) {
                manager.undo();
            }
        }
    });
    component.getActionMap().put("redo", new AbstractAction() {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (manager.canRedo()) {
                manager.redo();
            }
        }
    });
    KeyStroke commandZ = KeyStroke.getKeyStroke(KeyEvent.VK_Z, COMMAND_KEY);
    component.getInputMap().put(commandZ, "undo");
    KeyStroke shiftCommandZ = KeyStroke.getKeyStroke(KeyEvent.VK_Z, COMMAND_KEY | InputEvent.SHIFT_DOWN_MASK);
    component.getInputMap().put(shiftCommandZ, "redo");
}