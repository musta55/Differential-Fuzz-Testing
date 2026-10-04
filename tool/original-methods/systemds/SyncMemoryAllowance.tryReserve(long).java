@Override
public boolean tryReserve(long bytes) {
    long minRequest;
    long maxRequest;
    synchronized (this) {
        if (_shutdown || _destroyed)
            return false;
        if (_usedBytes + bytes <= _grantedBytes) {
            _usedBytes += bytes;
            return true;
        }
        if (_usedBytes + bytes > _targetBytes)
            return false;
        minRequest = _usedBytes + bytes - _grantedBytes;
        maxRequest = Math.max(minRequest, Math.max(_grantedBytes, bytes) * 2);
    }
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
            if (_usedBytes + bytes <= _targetBytes && _usedBytes + bytes <= _grantedBytes) {
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