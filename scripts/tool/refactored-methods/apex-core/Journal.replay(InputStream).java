final void replay(final InputStream input) {
    if (replayMode.compareAndSet(false, true)) {
        try (Input in = new Input(input)) {
            LOG.debug("Start replaying WAL");
            while (!in.eof()) {
                int opId = in.readInt();
                RecoverableOperation recoverableOperation = RecoverableOperation.get(opId);
                if (recoverableOperation == null) {
                    throw new IllegalArgumentException("No reader registered for id " + opId);
                }
                LOG.debug("Replaying {}", recoverableOperation);
                Object target = getTargetForOperation(recoverableOperation);
                recoverableOperation.operation.read(target, in);
            }
        } finally {
            LOG.debug("Done replaying WAL");
            replayMode.set(false);
        }
    } else {
        throw new IllegalStateException("Request to replay while journal is already replaying other operations");
    }
}
// ---- helper method(s) introduced by the refactoring ----
private Output createOutput(@Nullable OutputStream out) throws IOException {
    if (out != null) {
        Output output = new Output(4096, -1) {

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
        return output;
    } else {
        return null;
    }
}

private Object getTargetForOperation(RecoverableOperation recoverableOperation) {
    switch(recoverableOperation) {
        case OPERATOR_STATE:
        case CONTAINER_STATE:
            return scm.getPhysicalPlan();
        case OPERATOR_PROPERTY:
        case PHYSICAL_OPERATOR_PROPERTY:
            return scm;
        default:
            throw new IllegalArgumentException("Unsupported recoverable operation " + recoverableOperation);
    }
}

