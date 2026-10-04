public void setScopeChildren(boolean enableVariableButton) {
    childButton.setSelected(true);
    if (enableVariableButton) {
        //$NON-NLS-1$
        variableName.setText("");
    }
}