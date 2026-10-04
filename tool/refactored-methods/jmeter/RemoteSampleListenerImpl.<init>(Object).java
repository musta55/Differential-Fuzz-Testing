public RemoteSampleListenerImpl(Object listener) throws RemoteException {
    super(DEFAULT_LOCAL_PORT, RmiUtils.createClientSocketFactory(), RmiUtils.createServerSocketFactory());
    initializeListeners(listener);
}
// ---- helper method(s) introduced by the refactoring ----
private void initializeListeners(Object listener) {
    if (listener instanceof TestStateListener) {
        testListener = (TestStateListener) listener;
    }
    if (listener instanceof SampleListener) {
        sampleListener = (SampleListener) listener;
    }
}

