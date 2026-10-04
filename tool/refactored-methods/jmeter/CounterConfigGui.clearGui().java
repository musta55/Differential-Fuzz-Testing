/**
 * Implements JMeterGUIComponent.clearGui
 */
@Override
public void clearGui() {
    super.clearGui();
    clearFields();
}
// ---- helper method(s) introduced by the refactoring ----
private void setFieldsToConfig(CounterConfig config) {
    config.setStart(startField.getText());
    config.setEnd(endField.getText());
    config.setIncrement(incrField.getText());
    config.setVarName(varNameField.getText());
    config.setFormat(formatField.getText());
    config.setIsPerUser(perUserField.isSelected());
    config.setResetOnThreadGroupIteration(resetCounterOnEachThreadGroupIteration.isEnabled() && resetCounterOnEachThreadGroupIteration.isSelected());
}

private void clearFields() {
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

private void setFieldsFromConfig(CounterConfig config) {
    startField.setText(config.getStartAsString());
    endField.setText(config.getEndAsString());
    incrField.setText(config.getIncrementAsString());
    formatField.setText(config.getFormat());
    varNameField.setText(config.getVarName());
    perUserField.setSelected(config.isPerUser());
    updateResetCheckbox(config);
}

private void updateResetCheckbox(CounterConfig config) {
    if (config.isPerUser()) {
        resetCounterOnEachThreadGroupIteration.setEnabled(true);
        resetCounterOnEachThreadGroupIteration.setSelected(config.isResetOnThreadGroupIteration());
    } else {
        resetCounterOnEachThreadGroupIteration.setEnabled(false);
    }
}

