/**
 * Implements JMeterGUIComponent.clearGui
 */
@Override
public void clearGui() {
    super.clearGui();
    styleBox.setSelectedIndex(1);
    // $NON-NLS-1$
    throughput.setText("1");
    perthread.setSelected(false);
}