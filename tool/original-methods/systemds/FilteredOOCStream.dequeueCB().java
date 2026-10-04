@Override
public synchronized QueueCallback<T> dequeueCB() {
    if (_last != null)
        _last.close();
    while ((_last = _sourceStream.dequeueCB()) != null) {
        if (_predicate.apply(_last.get()))
            return _last;
        _last.close();
        _last = null;
    }
    return null;
}