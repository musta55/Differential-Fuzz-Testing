@Override
public void write(byte[] b, int off, int len) throws IOException {
    try {
        synchronized (_buff) {
            final byte[] b_pos = _buff[_pos];
            // block until buffer is free to use
            _locks[_pos].get();
            if (b_pos.length >= len) {
                // copy the block into the buffer.
                System.arraycopy(b, off, b_pos, 0, len);
                // submit write request guaranteed to be sequential since it is using a single thread.
                _locks[_pos] = _pool.submit(() -> writeBuffer(b_pos, 0, len));
                // copy for asynchronous write because b is reused higher up
            } else {
                // The given byte array is longer than the buffer.
                // This means that the async buffer would overflow and therefore not work.
                // To avoid this we simply write the given byte array without a buffer.
                // This approach only works if the caller adhere to not modify the byte array given
                _locks[_pos] = _pool.submit(() -> writeBuffer(b, off, len));
                // get the task to reduce the risk ( and at least block the current thread)
                // to avoid race conditions from callers.
                _locks[_pos].get();
            }
            _pos = (_pos + 1) % _buff.length;
        }
    } catch (Exception ex) {
        throw new IOException(ex);
    }
}