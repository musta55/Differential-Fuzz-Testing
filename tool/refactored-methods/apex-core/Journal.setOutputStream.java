public void setOutputStream(@Nullable final OutputStream out) throws IOException {
    final Output newOutput = createOutput(out);
    final Output oldOut = this.output.getAndSet(newOutput);
    if (oldOut != null && oldOut.getOutputStream() != out) {
        synchronized (oldOut) {
            oldOut.close();
        }
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

