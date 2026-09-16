/**
 * @see RemoteThreadsListener#threadStarted()
 */
@Override
public void threadStarted() {
    JMeterContextService.incrNumberOfThreads();
    updateGui();
    for (RemoteThreadsLifeCycleListener listener : listeners) {
        listener.threadNumberIncreased(JMeterContextService.getNumberOfThreads());
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static void updateGui() {
    GuiPackage gp = GuiPackage.getInstance();
    if (gp != null) {
        // check there is a GUI
        gp.getMainFrame().updateCounts();
    }
}

