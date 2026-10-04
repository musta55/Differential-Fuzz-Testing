@Override
public void threadStarted() {
    if (listener != null) {
        try {
            listener.threadStarted();
        } catch (RemoteException err) {
            // $NON-NLS-1$
            log.error("Exception invoking listener on threadStarted.", err);
        }
    }
}