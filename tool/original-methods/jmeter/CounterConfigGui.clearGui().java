/**
 * Implements JMeterGUIComponent.clearGui
 */
@Override
public void clearGui() {
    super.clearGui();
    //$NON-NLS-1$
    startField.setText("");
    //$NON-NLS-1$
    incrField.setText("");
    //$NON-NLS-1$
    endField.setText("");
    //$NON-NLS-1$
    varNameField.setText("");
    //$NON-NLS-1$
    formatField.setText("");
    perUserField.setSelected(false);
    resetCounterOnEachThreadGroupIteration.setEnabled(false);
}