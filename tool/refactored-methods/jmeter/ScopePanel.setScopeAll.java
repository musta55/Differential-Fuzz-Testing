public void setScopeAll(boolean enableVariableButton) {
    allButton.setSelected(true);
    if (enableVariableButton && variableName != null) {
        //$NON-NLS-1$
        variableName.setText("");
    }
}
// ---- helper method(s) introduced by the refactoring ----
public boolean isSelectedParent() {
    return parentButton.isSelected();
}

