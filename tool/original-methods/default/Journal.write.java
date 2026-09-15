final void write(Recoverable op) {
    if (replayMode.get()) {
        throw new IllegalStateException("Request to write while journal is replaying operations");
    }
    Integer classId = RecoverableOperation.getId(op.getClass());
    if (classId == null) {
        throw new IllegalArgumentException("Class not registered " + op.getClass());
    }
    while (true) {
        final Output out = output.get();
        if (out != null) {
            // need to atomically write id, operation and flush the output stream
            synchronized (out) {
                try {
                    LOG.debug("WAL write {}", RecoverableOperation.get(classId));
                    out.writeInt(classId);
                    op.write(out);
                    out.flush();
                    break;
                } catch (KryoException e) {
                    // check that no other threads sneaked between get() and synchronized block and set output stream to a new
                    // stream or null leading to the current stream being closed
                    if (output.get() == out) {
                        throw e;
                    }
                }
            }
        } else {
            LOG.warn("Journal output stream is null. Skipping write to the WAL.");
            break;
        }
    }
}