/**
 * @see org.apache.jmeter.gui.action.AbstractActionWithNoRunningTest#doActionAfterCheck(ActionEvent)
 */
@Override
public void doAction(ActionEvent e) {
    final String actionCommand = e.getActionCommand();
    float scale = JMeterUIDefaults.INSTANCE.getScale();
    if (actionCommand.equals(ActionNames.ZOOM_IN)) {
        scale *= ZOOM_SCALE;
    } else if (actionCommand.equals(ActionNames.ZOOM_OUT)) {
        scale /= ZOOM_SCALE;
    }
    JMeterUIDefaults.INSTANCE.setScale(scale);
    JMeterUtils.refreshUI();
}