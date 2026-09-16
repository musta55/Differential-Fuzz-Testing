/**
 * @see RemoteThreadsListener#threadStarted()
 */
@Override
public void threadStarted() {
    JMeterContextService.incrNumberOfThreads();
    GuiPackage gp = GuiPackage.getInstance();
    if (gp != null) {
        // check there is a GUI
        gp.getMainFrame().updateCounts();
    }
    for (RemoteThreadsLifeCycleListener listener : listeners) {
        listener.threadNumberIncreased(JMeterContextService.getNumberOfThreads());
    }
}