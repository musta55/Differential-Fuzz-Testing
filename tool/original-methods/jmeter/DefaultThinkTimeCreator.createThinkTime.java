@Override
public JMeterTreeNode[] createThinkTime(GuiPackage guiPackage, JMeterTreeNode parentNode) throws IllegalUserActionException {
    TestAction testAction = (TestAction) guiPackage.createTestElement(TestActionGui.class.getName());
    testAction.setAction(TestAction.PAUSE);
    testAction.setDuration("0");
    JMeterTreeNode thinkTimeNode = new JMeterTreeNode(testAction, guiPackage.getTreeModel());
    thinkTimeNode.setName("Think Time");
    RandomTimer randomTimer = (RandomTimer) guiPackage.createTestElement(DEFAULT_TIMER_IMPLEMENTATION);
    randomTimer.setDelay(DEFAULT_PAUSE);
    randomTimer.setRange(DEFAULT_RANGE);
    randomTimer.setName("Pause");
    JMeterTreeNode urtNode = new JMeterTreeNode(randomTimer, guiPackage.getTreeModel());
    return new JMeterTreeNode[] { thinkTimeNode, urtNode };
}