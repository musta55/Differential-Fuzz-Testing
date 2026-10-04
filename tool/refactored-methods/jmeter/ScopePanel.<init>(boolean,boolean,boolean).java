public ScopePanel(boolean enableVariableButton, boolean enableParentAndSubsamples, boolean enableSubsamplesOnly) {
    //$NON-NLS-1$
    parentButton = new JRadioButton(JMeterUtils.getResString("sample_scope_parent"));
    //$NON-NLS-1$
    allButton = enableParentAndSubsamples ? new JRadioButton(JMeterUtils.getResString("sample_scope_all")) : null;
    //$NON-NLS-1$
    childButton = enableSubsamplesOnly ? new JRadioButton(JMeterUtils.getResString("sample_scope_children")) : null;
    if (enableVariableButton) {
        //$NON-NLS-1$
        variableButton = new JRadioButton(JMeterUtils.getResString("sample_scope_variable"));
        variableName = new JTextField(10);
    } else {
        variableButton = null;
        variableName = null;
    }
    init();
}
// ---- helper method(s) introduced by the refactoring ----
public boolean isSelectedParent() {
    return parentButton.isSelected();
}

