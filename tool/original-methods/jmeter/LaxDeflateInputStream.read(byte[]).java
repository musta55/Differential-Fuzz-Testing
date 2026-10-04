@Override
public int read(byte[] b) throws IOException {
    try {
        return super.read(b);
    } catch (final EOFException ex) {
        return handleRelaxMode(ex, relax);
    }
}