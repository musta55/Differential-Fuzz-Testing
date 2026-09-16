/**
 * @see org.apache.jmeter.gui.action.AbstractActionWithNoRunningTest#doActionAfterCheck(ActionEvent)
 */
@Override
public void doAction(ActionEvent e) {
    final String actionCommand = e.getActionCommand();
    float scale = JMeterUIDefaults.INSTANCE.getScale();
    scale = calculateNewScale(actionCommand, scale);
    JMeterUIDefaults.INSTANCE.setScale(scale);
    JMeterUtils.refreshUI();
}
// ---- helper method(s) introduced by the refactoring ----
private static float calculateNewScale(String actionCommand, float currentScale) {
    if (actionCommand.equals(ActionNames.ZOOM_IN)) {
        return currentScale * ZOOM_SCALE;
    } else if (actionCommand.equals(ActionNames.ZOOM_OUT)) {
        return currentScale / ZOOM_SCALE;
    }
    return currentScale;
}

