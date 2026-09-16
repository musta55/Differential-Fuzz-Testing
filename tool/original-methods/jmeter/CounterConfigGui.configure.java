@Override
public void configure(TestElement element) {
    super.configure(element);
    CounterConfig config = (CounterConfig) element;
    startField.setText(config.getStartAsString());
    endField.setText(config.getEndAsString());
    incrField.setText(config.getIncrementAsString());
    formatField.setText(config.getFormat());
    varNameField.setText(config.getVarName());
    perUserField.setSelected(config.isPerUser());
    if (config.isPerUser()) {
        resetCounterOnEachThreadGroupIteration.setEnabled(true);
        resetCounterOnEachThreadGroupIteration.setSelected(config.isResetOnThreadGroupIteration());
    } else {
        resetCounterOnEachThreadGroupIteration.setEnabled(false);
    }
}