final void write(Recoverable op) {
    if (replayMode.get()) {
        throw new IllegalStateException("Request to write while journal is replaying operations");
    }
    Integer classId = RecoverableOperation.getId(op.getClass());
    if (classId == null) {
        throw new IllegalArgumentException("Class not registered " + op.getClass());
    }
    Output out = output.get();
    if (out != null) {
        synchronized (out) {
            try {
                LOG.debug("WAL write {}", RecoverableOperation.get(classId));
                out.writeInt(classId);
                op.write(out);
                out.flush();
            } catch (KryoException e) {
                if (output.get() == out) {
                    throw e;
                }
            }
        }
    } else {
        LOG.warn("Journal output stream is null. Skipping write to the WAL.");
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

