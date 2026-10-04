/* (non-Javadoc)
     * @see org.apache.jmeter.samplers.RemoteThreadsListener#threadFinished()
     */
@Override
public void threadFinished() {
    JMeterContextService.decrNumberOfThreads();
    updateGui();
    for (RemoteThreadsLifeCycleListener listener : listeners) {
        listener.threadNumberDecreased(JMeterContextService.getNumberOfThreads());
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

