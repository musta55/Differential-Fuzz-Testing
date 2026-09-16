@Override
public void propertyChange(PropertyChangeEvent evt) {
    manager.discardAllEdits();
    removeUndoableEditListener(evt.getOldValue());
    addUndoableEditListener(evt.getNewValue());
}
// ---- helper method(s) introduced by the refactoring ----
private void removeUndoableEditListener(Object oldValue) {
    if (oldValue != null) {
        ((Document) oldValue).removeUndoableEditListener(manager);
    }
}

private void addUndoableEditListener(Object newValue) {
    if (newValue != null) {
        ((Document) newValue).addUndoableEditListener(manager);
    }
}

