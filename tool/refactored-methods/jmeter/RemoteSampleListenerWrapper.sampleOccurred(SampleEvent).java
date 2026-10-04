@Override
public void sampleOccurred(SampleEvent e) {
    try {
        if (listener != null) {
            listener.sampleOccurred(e);
        }
    } catch (RemoteException err) {
        // $NON-NLS-1$
        log.error("RemoteException while handling sample occurred event.", err);
    }
}