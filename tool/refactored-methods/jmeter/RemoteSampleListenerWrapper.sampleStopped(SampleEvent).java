@Override
public void sampleStopped(SampleEvent e) {
    try {
        if (listener != null) {
            listener.sampleStopped(e);
        }
    } catch (RemoteException err) {
        // $NON-NLS-1$
        log.error("RemoteException while handling sample stopped event.", err);
    }
}