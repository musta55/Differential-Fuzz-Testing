@Override
public void testEnded() {
    synchronized (this) {
        if (driver != null) {
            driver.close();
            driver = null;
        }
    }
}