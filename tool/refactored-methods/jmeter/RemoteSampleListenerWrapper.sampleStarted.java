@Override
public void sampleStarted(SampleEvent e) {
    try {
        if (listener != null) {
            listener.sampleStarted(e);
        }
    } catch (RemoteException err) {
        // $NON-NLS-1$
        log.error("RemoteException while handling sample started event.", err);
    }
}