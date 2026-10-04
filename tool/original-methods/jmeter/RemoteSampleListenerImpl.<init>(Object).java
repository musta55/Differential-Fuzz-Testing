public RemoteSampleListenerImpl(Object listener) throws RemoteException {
    super(DEFAULT_LOCAL_PORT, RmiUtils.createClientSocketFactory(), RmiUtils.createServerSocketFactory());
    if (listener instanceof TestStateListener) {
        testListener = (TestStateListener) listener;
    } else {
        testListener = null;
    }
    if (listener instanceof SampleListener) {
        sampleListener = (SampleListener) listener;
    } else {
        sampleListener = null;
    }
}