@Override
public synchronized QueueCallback<T> dequeueCB() {
    if (_last != null)
        _last.close();
    _last = _taskQueue.dequeueCB();
    return _last;
}