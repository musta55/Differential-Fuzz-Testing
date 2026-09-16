@Override
public void focusGained(FocusEvent focusEvent) {
    variableButton.setSelected(focusEvent.getSource() == variableName);
}