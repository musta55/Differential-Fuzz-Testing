@Override
public void doAction(ActionEvent e) throws IllegalUserActionException {
    JMeterTreeNode node = GuiPackage.getInstance().getTreeListener().getCurrentNode();
    TestElement te = (TestElement) node.getUserObject();
    if (ActionNames.WHAT_CLASS.equals(e.getActionCommand())) {
        String guiClassName = te.getPropertyAsString(TestElement.GUI_CLASS);
        System.out.println(te.getClass().getName());
        System.out.println(guiClassName);
        if (log.isInfoEnabled()) {
            log.info("TestElement: {}, guiClassName: {}", te.getClass(), guiClassName);
        }
    } else if (ActionNames.DEBUG_ON.equals(e.getActionCommand())) {
        final String loggerName = te.getClass().getName();
        Configurator.setAllLevels(loggerName, Level.DEBUG);
        log.info("Log level set to DEBUG for {}", loggerName);
    } else if (ActionNames.DEBUG_OFF.equals(e.getActionCommand())) {
        final String loggerName = te.getClass().getName();
        Configurator.setAllLevels(loggerName, Level.INFO);
        log.info("Log level set to INFO for {}", loggerName);
    } else if (ActionNames.HEAP_DUMP.equals(e.getActionCommand())) {
        try {
            String s = HeapDumper.dumpHeap();
            JOptionPane.showMessageDialog(null, "Created " + s, "HeapDump", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            // NOSONAR We show cause in message
            JOptionPane.showMessageDialog(null, ex.toString(), "HeapDump", JOptionPane.ERROR_MESSAGE);
        }
    } else if (ActionNames.THREAD_DUMP.equals(e.getActionCommand())) {
        try {
            String s = ThreadDumper.threadDump();
            JOptionPane.showMessageDialog(null, "Created " + s, "ThreadDump", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            // NOSONAR We show cause in message
            JOptionPane.showMessageDialog(null, ex.toString(), "ThreadDump", JOptionPane.ERROR_MESSAGE);
        }
    }
}