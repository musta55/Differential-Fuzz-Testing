/* (non-Javadoc)
     * @see org.apache.http.client.entity.DeflateInputStream#read(byte[], int, int)
     */
@Override
public int read(byte[] b, int off, int len) throws IOException {
    try {
        return super.read(b, off, len);
    } catch (final EOFException ex) {
        return handleRelaxMode(ex, relax);
    }
}