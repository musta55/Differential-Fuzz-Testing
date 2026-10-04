@Override
public boolean tryReserve(long bytes) {
    long[] request = calculateRequest(bytes);
    long minRequest = request[0];
    long maxRequest = request[1];
    if (bytes > _consumptionLimit)
        throw new IllegalArgumentException("Cannot reserve more memory than the consumption limit");
    long granted = _broker.requestMemory(this, minRequest, maxRequest);
    long refund = 0;
    boolean success = false;
    boolean drainWaiters = false;
    synchronized (this) {
        if (_shutdown || _destroyed)
            refund = granted;
        else {
            _grantedBytes += granted;
            if (canReserve(bytes)) {
                _usedBytes += bytes;
                success = true;
            }
            drainWaiters = success && !_reservationWaiters.isEmpty();
            notifyAll();
        }
    }
    if (refund > 0)
        _broker.freeMemory(this, refund);
    if (drainWaiters)
        requestReservationDrain();
    return success;
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

