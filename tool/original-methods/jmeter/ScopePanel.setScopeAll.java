public void setScopeAll(boolean enableVariableButton) {
    allButton.setSelected(true);
    if (enableVariableButton) {
        //$NON-NLS-1$
        variableName.setText("");
    }
}