/* (non-Javadoc)
     * @see org.apache.jmeter.gui.action.AbstractAction#doAction(java.awt.event.ActionEvent)
     */
@Override
public void doAction(ActionEvent e) {
    GuiPackage instance = GuiPackage.getInstance();
    List<Stoppable> stopables = instance.getStoppables();
    for (Stoppable element : stopables) {
        instance.unregister(element);
        element.stopServer();
    }
}