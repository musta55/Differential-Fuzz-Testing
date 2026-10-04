@Override
public void release(long bytes) {
    long freedMemory = 0;
    long destroyFreedMemory = 0;
    boolean destroy = false;
    boolean drainWaiters;
    synchronized (this) {
        if (bytes < 0)
            throw new IllegalArgumentException("Cannot release negative bytes: " + bytes);
        if (_usedBytes < bytes)
            throw new IllegalArgumentException("Memory allowance underflow in " + getClass().getSimpleName() + ": release=" + bytes + ", used=" + _usedBytes + ", granted=" + _grantedBytes + ", target=" + _targetBytes + ", shutdown=" + _shutdown + ", destroyed=" + _destroyed);
        _usedBytes -= bytes;
        if (_shutdown) {
            long oldGrantedBytes = _grantedBytes;
            _grantedBytes = _usedBytes;
            if (_grantedBytes < 0)
                throw new IllegalArgumentException("Granted memory underflow in " + getClass().getSimpleName() + ": granted=" + _grantedBytes + ", used=" + _usedBytes + ", released=" + bytes);
            if (_usedBytes == 0) {
                _destroyed = true;
                destroy = true;
                destroyFreedMemory = oldGrantedBytes;
            } else {
                freedMemory = oldGrantedBytes - _grantedBytes;
            }
        } else if (_grantedBytes > _targetBytes) {
            long oldGrantedBytes = _grantedBytes;
            _grantedBytes = Math.max(_usedBytes, _targetBytes);
            freedMemory = oldGrantedBytes - _grantedBytes;
        } else if (_usedBytes * 3 < _grantedBytes * 2) {
            long oldGrantedBytes = _grantedBytes;
            _grantedBytes = Math.max(_usedBytes, Math.min(_grantedBytes, _usedBytes + RELEASE_TRIM_BUFFER_BYTES));
            freedMemory = oldGrantedBytes - _grantedBytes;
        }
        drainWaiters = !_reservationWaiters.isEmpty() && !_shutdown && !_destroyed;
        notifyAll();
    }
    if (destroy)
        _broker.destroyAllowance(this, destroyFreedMemory);
    else if (freedMemory > 0)
        _broker.freeMemory(this, freedMemory);
    if (drainWaiters)
        requestReservationDrain();
}
// ---- helper method(s) introduced by the refactoring ----
private long[] calculateRequest(long bytes) {
    long minRequest;
    long maxRequest;
    synchronized (this) {
        if (_shutdown || _destroyed)
            return new long[] { 0, 0 };
        if (_usedBytes + bytes <= _grantedBytes)
            return new long[] { 0, 0 };
        if (_usedBytes + bytes > _targetBytes)
            return new long[] { 0, 0 };
        minRequest = _usedBytes + bytes - _grantedBytes;
        maxRequest = Math.max(minRequest, Math.max(_grantedBytes, bytes) * 2);
    }
    return new long[] { minRequest, maxRequest };
}

private boolean canReserve(long bytes) {
    return _usedBytes + bytes <= _targetBytes && _usedBytes + bytes <= _grantedBytes;
}

