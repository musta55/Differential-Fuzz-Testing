public final int getAndIncrement() {
    int current;
    do {
        current = this.value.get();
        int next = (current >= endValue) ? startValue : current + 1;
        if (this.value.compareAndSet(current, next)) {
            return current;
        }
    } while (true);
}