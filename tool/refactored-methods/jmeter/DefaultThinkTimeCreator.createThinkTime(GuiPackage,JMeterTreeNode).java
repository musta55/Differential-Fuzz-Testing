@Override
public JMeterTreeNode[] createThinkTime(GuiPackage guiPackage, JMeterTreeNode parentNode) throws IllegalUserActionException {
    JMeterTreeNode testActionNode = createTestActionNode(guiPackage);
    JMeterTreeNode randomTimerNode = createRandomTimerNode(guiPackage);
    return new JMeterTreeNode[] { testActionNode, randomTimerNode };
}
// ---- helper method(s) introduced by the refactoring ----
private static JMeterTreeNode createTestActionNode(GuiPackage guiPackage) {
    TestAction testAction = (TestAction) guiPackage.createTestElement(TestActionGui.class.getName());
    testAction.setAction(TestAction.PAUSE);
    testAction.setDuration("0");
    JMeterTreeNode thinkTimeNode = new JMeterTreeNode(testAction, guiPackage.getTreeModel());
    thinkTimeNode.setName("Think Time");
    return thinkTimeNode;
}

private static JMeterTreeNode createRandomTimerNode(GuiPackage guiPackage) {
    RandomTimer randomTimer = (RandomTimer) guiPackage.createTestElement(DEFAULT_TIMER_IMPLEMENTATION);
    randomTimer.setDelay(DEFAULT_PAUSE);
    randomTimer.setRange(DEFAULT_RANGE);
    randomTimer.setName("Pause");
    return new JMeterTreeNode(randomTimer, guiPackage.getTreeModel());
}

