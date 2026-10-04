@Override
public void threadFinished() {
    try {
        listener.threadFinished();
    } catch (RemoteException err) {
        // $NON-NLS-1$
        log.error("Exception invoking listener on threadFinished.", err);
    }
}