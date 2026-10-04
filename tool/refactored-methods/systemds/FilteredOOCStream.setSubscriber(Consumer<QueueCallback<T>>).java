@Override
public void setSubscriber(Consumer<QueueCallback<T>> subscriber) {
    _sourceStream.setSubscriber(cb -> {
        if (isFinalCallback(cb)) {
            subscriber.accept(cb);
            return;
        }
        if (isGroupCallback(cb)) {
            handleGroupCallback((OOCStream.GroupQueueCallback<T>) cb, subscriber);
            return;
        }
        if (_predicate.apply(cb.get()))
            subscriber.accept(cb);
        else
            cb.close();
    });
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

