@Override
public void put(Object tuple) {
    try {
        reservoir.put(tuple);
        if (!(tuple instanceof Tuple)) {
            count++;
        }
    } catch (InterruptedException ie) {
        logger.debug("Interrupted", ie);
        throw new RuntimeException(ie);
    }
}