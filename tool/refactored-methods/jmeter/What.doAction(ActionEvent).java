@Override
public void doAction(ActionEvent e) throws IllegalUserActionException {
    JMeterTreeNode node = GuiPackage.getInstance().getTreeListener().getCurrentNode();
    TestElement te = (TestElement) node.getUserObject();
    String actionCommand = e.getActionCommand();
    switch(actionCommand) {
        case ActionNames.WHAT_CLASS:
            displayTestClassDetails(te);
            break;
        case ActionNames.DEBUG_ON:
            setLogLevel(te, Level.DEBUG);
            break;
        case ActionNames.DEBUG_OFF:
            setLogLevel(te, Level.INFO);
            break;
        case ActionNames.HEAP_DUMP:
            performHeapDump();
            break;
        case ActionNames.THREAD_DUMP:
            performThreadDump();
            break;
        default:
            throw new IllegalUserActionException("Unknown action command: " + actionCommand);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static void displayTestClassDetails(TestElement te) {
    String guiClassName = te.getPropertyAsString(TestElement.GUI_CLASS);
    System.out.println(te.getClass().getName());
    System.out.println(guiClassName);
    if (log.isInfoEnabled()) {
        log.info("TestElement: {}, guiClassName: {}", te.getClass(), guiClassName);
    }
}

private static void setLogLevel(TestElement te, Level level) {
    final String loggerName = te.getClass().getName();
    Configurator.setAllLevels(loggerName, level);
    log.info("Log level set to {} for {}", level, loggerName);
}

private static void performHeapDump() {
    try {
        String s = HeapDumper.dumpHeap();
        JOptionPane.showMessageDialog(null, "Created " + s, "HeapDump", JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception ex) {
        // NOSONAR We show cause in message
        JOptionPane.showMessageDialog(null, ex.toString(), "HeapDump", JOptionPane.ERROR_MESSAGE);
    }
}

private static void performThreadDump() {
    try {
        String s = ThreadDumper.threadDump();
        JOptionPane.showMessageDialog(null, "Created " + s, "ThreadDump", JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception ex) {
        // NOSONAR We show cause in message
        JOptionPane.showMessageDialog(null, ex.toString(), "ThreadDump", JOptionPane.ERROR_MESSAGE);
    }
}

