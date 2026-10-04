@Override
public synchronized T dequeue() {
    if (_last != null)
        _last.close();
    _last = _taskQueue.dequeueCB();
    if (_last != null)
        return _last.get();
    return null;
}