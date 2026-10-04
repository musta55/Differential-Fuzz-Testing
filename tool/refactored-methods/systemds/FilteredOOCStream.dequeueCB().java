@Override
public synchronized QueueCallback<T> dequeueCB() {
    closeLastCallback();
    while ((_last = _sourceStream.dequeueCB()) != null) {
        if (_predicate.apply(_last.get()))
            return _last;
        closeLastCallback();
    }
    return null;
}
// ---- helper method(s) introduced by the refactoring ----
private void closeLastCallback() {
    if (_last != null) {
        _last.close();
        _last = null;
    }
}

private boolean isFinalCallback(QueueCallback<T> cb) {
    return cb.isFailure() || cb.isEos();
}

private boolean isGroupCallback(QueueCallback<T> cb) {
    return cb instanceof OOCStream.GroupQueueCallback<?>;
}

private void handleGroupCallback(OOCStream.GroupQueueCallback<T> group, Consumer<QueueCallback<T>> subscriber) {
    for (int i = 0; i < group.size(); i++) {
        QueueCallback<T> sub = group.getCallback(i);
        if (isFinalCallback(sub) || _predicate.apply(sub.get()))
            subscriber.accept(sub);
        else
            sub.close();
    }
}

