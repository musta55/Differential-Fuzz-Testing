@Override
public void threadFinished() {
    if (listener != null) {
        try {
            listener.threadFinished();
        } catch (RemoteException err) {
            // $NON-NLS-1$
            log.error("Exception invoking listener on threadFinished.", err);
        }
    }
}