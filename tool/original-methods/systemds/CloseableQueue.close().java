/**
 * Close queue for N consumers.
 * Each consumer will receive exactly one poison pill and then should stop.
 */
public boolean close() throws InterruptedException {
    synchronized (this) {
        if (closed)
            // idempotent
            return false;
        closed = true;
    }
    queue.put(POISON);
    return true;
}