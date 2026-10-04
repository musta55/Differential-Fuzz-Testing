/**
 * Notify all registered change listeners that the text in the text field
 * has changed.
 */
private void notifyChangeListeners() {
    ChangeEvent ce = new ChangeEvent(this);
    for (ChangeListener listener : mChangeListeners) {
        listener.stateChanged(ce);
    }
}