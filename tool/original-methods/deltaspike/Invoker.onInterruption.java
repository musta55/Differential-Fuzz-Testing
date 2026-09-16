private static Semaphore onInterruption(final InterruptedException e) {
    Thread.interrupted();
    throw ExceptionUtils.throwAsRuntimeException(e);
}