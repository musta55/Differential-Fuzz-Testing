/* (non-Javadoc)
     * @see org.apache.jmeter.samplers.RemoteThreadsListener#threadFinished()
     */
@Override
public void threadFinished() {
    JMeterContextService.decrNumberOfThreads();
    GuiPackage gp = GuiPackage.getInstance();
    if (gp != null) {
        // check there is a GUI
        gp.getMainFrame().updateCounts();
    }
    for (RemoteThreadsLifeCycleListener listener : listeners) {
        listener.threadNumberDecreased(JMeterContextService.getNumberOfThreads());
    }
}