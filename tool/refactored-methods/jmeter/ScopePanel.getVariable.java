public String getVariable() {
    //$NON-NLS-1$
    return variableName != null ? variableName.getText() : "";
}
// ---- helper method(s) introduced by the refactoring ----
public boolean isSelectedParent() {
    return parentButton.isSelected();
}

