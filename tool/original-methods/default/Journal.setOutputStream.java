public void setOutputStream(@Nullable final OutputStream out) throws IOException {
    final Output output;
    if (out != null) {
        output = new Output(4096, -1) {

            @Override
            public void flush() throws KryoException {
                super.flush();
                // Kryo does not flush internal output stream during flush. We need to flush it explicitly.
                try {
                    getOutputStream().flush();
                } catch (IOException e) {
                    throw new KryoException(e);
                }
            }
        };
        output.setOutputStream(out);
    } else {
        output = null;
    }
    final Output oldOut = this.output.getAndSet(output);
    if (oldOut != null && oldOut.getOutputStream() != out) {
        synchronized (oldOut) {
            oldOut.close();
        }
    }
}