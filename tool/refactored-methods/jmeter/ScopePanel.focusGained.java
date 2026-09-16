@Override
public void focusGained(FocusEvent focusEvent) {
    if (variableButton != null && variableName != null) {
        variableButton.setSelected(focusEvent.getSource() == variableName);
    }
}
// ---- helper method(s) introduced by the refactoring ----
public boolean isSelectedParent() {
    return parentButton.isSelected();
}

