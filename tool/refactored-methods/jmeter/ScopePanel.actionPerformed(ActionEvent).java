@Override
public void actionPerformed(ActionEvent e) {
    if (variableButton != null && variableName != null) {
        variableName.setEnabled(variableButton.isSelected());
    }
}
// ---- helper method(s) introduced by the refactoring ----
public boolean isSelectedParent() {
    return parentButton.isSelected();
}

