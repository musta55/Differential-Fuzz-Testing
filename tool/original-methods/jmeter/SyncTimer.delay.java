/**
 * {@inheritDoc}
 */
@Override
public long delay() {
    if (getGroupSize() >= 0) {
        int arrival = 0;
        try {
            if (timeoutInMs == 0) {
                arrival = this.barrier.await(TimerService.getInstance().adjustDelay(Long.MAX_VALUE), TimeUnit.MILLISECONDS);
            } else if (timeoutInMs > 0) {
                arrival = this.barrier.await(TimerService.getInstance().adjustDelay(timeoutInMs), TimeUnit.MILLISECONDS);
            } else {
                throw new IllegalArgumentException("Negative value for timeout:" + timeoutInMs + " in Synchronizing Timer " + getName());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return 0;
        } catch (BrokenBarrierException e) {
            return 0;
        } catch (TimeoutException e) {
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