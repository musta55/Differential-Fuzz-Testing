/**
 * Close queue for N consumers.
 * Each consumer will receive exactly one poison pill and then should stop.
 */
public boolean close() throws InterruptedException {
    if (closed)
        // idempotent
        return false;
    closed = true;
    queue.put(POISON);
    return true;
}