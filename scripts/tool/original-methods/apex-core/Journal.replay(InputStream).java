final void replay(final InputStream input) {
    if (replayMode.compareAndSet(false, true)) {
        Input in = new Input(input);
        try {
            LOG.debug("Start replaying WAL");
            while (!in.eof()) {
                final int opId = in.readInt();
                final RecoverableOperation recoverableOperation = RecoverableOperation.get(opId);
                if (recoverableOperation == null) {
                    throw new IllegalArgumentException("No reader registered for id " + opId);
                }
                LOG.debug("Replaying {}", recoverableOperation);
                switch(recoverableOperation) {
                    case OPERATOR_STATE:
                    case CONTAINER_STATE:
                        recoverableOperation.operation.read(scm.getPhysicalPlan(), in);
                        break;
                    case OPERATOR_PROPERTY:
                    case PHYSICAL_OPERATOR_PROPERTY:
                        recoverableOperation.operation.read(scm, in);
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported recoverable operation " + recoverableOperation);
                }
            }
        } finally {
            LOG.debug("Done replaying WAL");
            replayMode.set(false);
        }
    } else {
        throw new IllegalStateException("Request to replay while journal is already replaying other operations");
    }
}