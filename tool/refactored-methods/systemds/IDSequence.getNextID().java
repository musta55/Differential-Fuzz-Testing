/**
 * Creates the next ID, if overflow a RuntimeException is thrown.
 *
 * @return ID
 */
public long getNextID() {
    long val = _current.incrementAndGet();
    if (val == _cycleLen) {
        if (!_cyclic)
            throw new IllegalStateException("IDSequence has reached its maximum value and is not cyclic.");
        reset();
    }
    return val;
}