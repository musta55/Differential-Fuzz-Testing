public void setScopeParent(boolean enableVariableButton) {
    parentButton.setSelected(true);
    if (enableVariableButton) {
        //$NON-NLS-1$
        variableName.setText("");
    }
}