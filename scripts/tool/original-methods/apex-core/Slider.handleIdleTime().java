@Override
public void handleIdleTime() {
    if (unifier instanceof IdleTimeHandler) {
        ((IdleTimeHandler) unifier).handleIdleTime();
    } else {
        try {
            Thread.sleep(spinMillis);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
}