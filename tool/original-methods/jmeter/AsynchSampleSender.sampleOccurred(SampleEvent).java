@Override
public void sampleOccurred(SampleEvent e) {
    try {
        if (!queue.offer(e)) {
            // we failed to add the element first time
            queueWaits++;
            long t1 = System.nanoTime();
            queue.put(e);
            long t2 = System.nanoTime();
            queueWaitTime += t2 - t1;
        }
    } catch (Exception err) {
        log.error("sampleOccurred; failed to queue the sample", err);
    }
}