@Override
public double next() {
    if ((batchItemIndex == 0 && !events.hasRemaining()) || !valuesAreEqualWithPrecision(throughputProvider.getThroughput(), lastThroughput)) {
        generateNext();
    }
    if (batchSize == 1) {
        return events.get();
    }
    batchItemIndex++;
    if (batchItemIndex == 1) {
        // The first item advances the position
        return events.get();
    }
    if (batchItemIndex == batchSize) {
        batchItemIndex = 0;
    }
    // All the other items in the batch refer to the previous position
    // since #position() points to the next item to be returned
    return events.get(events.position() - 1);
}