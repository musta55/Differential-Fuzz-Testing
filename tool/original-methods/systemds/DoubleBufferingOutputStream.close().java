@Override
public void close() throws IOException {
    _pool.shutdown();
    super.close();
}