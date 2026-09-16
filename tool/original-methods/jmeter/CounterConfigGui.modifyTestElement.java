/**
 * Modifies a given TestElement to mirror the data in the gui components.
 *
 * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
 */
@Override
public void modifyTestElement(TestElement c) {
    if (c instanceof CounterConfig) {
        CounterConfig config = (CounterConfig) c;
        config.setStart(startField.getText());
        config.setEnd(endField.getText());
        config.setIncrement(incrField.getText());
        config.setVarName(varNameField.getText());
        config.setFormat(formatField.getText());
        config.setIsPerUser(perUserField.isSelected());
        config.setResetOnThreadGroupIteration(resetCounterOnEachThreadGroupIteration.isEnabled() && resetCounterOnEachThreadGroupIteration.isSelected());
    }
    super.configureTestElement(c);
}