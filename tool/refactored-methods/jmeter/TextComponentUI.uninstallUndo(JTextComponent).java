/**
 * Removes the default undo manager.
 * By default, JMeter installs undo manager to all text fields via {@code Swing -> createUI},
 * however, undo is not always needed (e.g. log panel), so here's an API to remove it.
 * @param component JTextField or JTextArea
 */
@API(since = "5.5", status = API.Status.INTERNAL)
public static void uninstallUndo(JTextComponent component) {
    for (PropertyChangeListener listener : component.getPropertyChangeListeners("document")) {
        if (listener instanceof AddUndoableEditListenerPropertyChangeListener) {
            AddUndoableEditListenerPropertyChangeListener v = (AddUndoableEditListenerPropertyChangeListener) listener;
            UndoManager undoManager = v.getUndoManager();
            undoManager.discardAllEdits();
            Document document = component.getDocument();
            if (document != null) {
                document.removeUndoableEditListener(undoManager);
            }
            component.removePropertyChangeListener("document", listener);
        }
    }
}