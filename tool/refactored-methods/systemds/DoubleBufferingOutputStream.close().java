@Override
public void close() {
    _pool.shutdown();
    try {
        out.close();
    } catch (Exception ex) {
        throw new RuntimeException(ex);
    }
}