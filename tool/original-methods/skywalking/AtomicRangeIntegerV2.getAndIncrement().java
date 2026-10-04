public final int getAndIncrement() {
    int next;
    do {
        next = this.value.incrementAndGet();
        if (next > endValue && this.value.compareAndSet(next, startValue)) {
            return endValue;
        }
    } while (next > endValue);
    return next - 1;
}