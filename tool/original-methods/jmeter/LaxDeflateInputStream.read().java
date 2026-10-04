@Override
public int read() throws IOException {
    try {
        return super.read();
    } catch (final EOFException ex) {
        return handleRelaxMode(ex, relax);
    }
}