/* (non-Javadoc)
     * @see org.apache.jmeter.gui.action.AbstractAction#doAction(java.awt.event.ActionEvent)
     */
@Override
public void doAction(ActionEvent e) {
    GuiPackage instance = GuiPackage.getInstance();
    List<Stoppable> stopables = instance.getStoppables();
    for (Stoppable element : stopables) {
        stopStoppable(instance, element);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static void stopStoppable(GuiPackage instance, Stoppable stoppable) {
    instance.unregister(stoppable);
    stoppable.stopServer();
}

