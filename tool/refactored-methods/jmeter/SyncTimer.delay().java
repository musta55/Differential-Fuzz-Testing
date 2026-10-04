/**
 * {@inheritDoc}
 */
@Override
public long delay() {
    if (getGroupSize() >= 0) {
        int arrival = 0;
        try {
            long adjustedTimeout = TimerService.getInstance().adjustDelay(timeoutInMs > 0 ? timeoutInMs : Long.MAX_VALUE);
            arrival = this.barrier.await(adjustedTimeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return 0;
        } catch (BrokenBarrierException | TimeoutException e) {
            if (log.isWarnEnabled()) {
                log.warn("SyncTimer {} timeouted waiting for users after: {}ms", getName(), getTimeoutInMs());
            }
            return 0;
        } finally {
            if (arrival == 0) {
                barrier.reset();
            }
        }
    }
    return 0;
}