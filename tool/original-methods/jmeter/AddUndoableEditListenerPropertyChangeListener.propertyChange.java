@Override
public void propertyChange(PropertyChangeEvent evt) {
    manager.discardAllEdits();
    if (evt.getOldValue() != null) {
        ((Document) evt.getOldValue()).removeUndoableEditListener(manager);
    }
    if (evt.getNewValue() != null) {
        ((Document) evt.getNewValue()).addUndoableEditListener(manager);
    }
}