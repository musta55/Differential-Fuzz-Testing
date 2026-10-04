@Override
public synchronized QueueCallback<T> dequeueCB() {
    closeLastCallback();
    _last = _taskQueue.dequeueCB();
    return _last;
}
// ---- helper method(s) introduced by the refactoring ----
private void closeLastCallback() {
    if (_last != null) {
        _last.close();
    }
}

private void handleCallback(Consumer<QueueCallback<T>> subscriber, QueueCallback<T> cb) {
    if (cb.isEos()) {
        subscriber.accept(OOCStream.eos(null));
    } else if (cb.isFailure()) {
        handleFailureCallback(subscriber, cb);
    } else {
        subscriber.accept(cb);
    }
}

private void handleFailureCallback(Consumer<QueueCallback<T>> subscriber, QueueCallback<T> cb) {
    try {
        cb.get();
        subscriber.accept(OOCStream.eos(new DMLRuntimeException("Stream callback indicated failure without cause")));
    } catch (DMLRuntimeException re) {
        subscriber.accept(OOCStream.eos(re));
    }
}

