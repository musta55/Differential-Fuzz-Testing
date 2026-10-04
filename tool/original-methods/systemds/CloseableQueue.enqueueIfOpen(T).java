/**
 * Enqueue if the queue is not closed.
 * @return false if already closed
 */
public boolean enqueueIfOpen(T task) throws InterruptedException {
    if (task == null)
        throw new IllegalArgumentException("null tasks not allowed");
    synchronized (this) {
        if (closed)
            return false;
        queue.put(task);
    }
    return true;
}