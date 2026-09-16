public void setScopeVariable(String value) {
    variableButton.setSelected(true);
    if (variableName != null) {
        variableName.setText(value);
    }
}
// ---- helper method(s) introduced by the refactoring ----
public boolean isSelectedParent() {
    return parentButton.isSelected();
}

