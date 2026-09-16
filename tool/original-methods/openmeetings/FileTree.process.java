void process(Predicate<T> invalid, Consumer<T> consumer) {
    if (item != null) {
        if (invalid.test(item)) {
            // we will not process invalid and i's children
            return;
        }
        consumer.accept(item);
    }
    if (!children.isEmpty()) {
        children.forEach((id, e) -> e.process(invalid, consumer));
    }
}