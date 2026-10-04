/* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
@Override
public void actionPerformed(ActionEvent e) {
    doAction(e);
}
// ---- helper method(s) introduced by the refactoring ----
private static void stopStoppable(GuiPackage instance, Stoppable stoppable) {
    instance.unregister(stoppable);
    stoppable.stopServer();
}

