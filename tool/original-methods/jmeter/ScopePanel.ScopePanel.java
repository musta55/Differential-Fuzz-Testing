public ScopePanel(boolean enableVariableButton, boolean enableParentAndSubsamples, boolean enableSubsamplesOnly) {
    //$NON-NLS-1$
    parentButton = new JRadioButton(JMeterUtils.getResString("sample_scope_parent"));
    if (enableParentAndSubsamples) {
        //$NON-NLS-1$
        allButton = new JRadioButton(JMeterUtils.getResString("sample_scope_all"));
    } else {
        allButton = null;
    }
    if (enableSubsamplesOnly) {
        //$NON-NLS-1$
        childButton = new JRadioButton(JMeterUtils.getResString("sample_scope_children"));
    } else {
        childButton = null;
    }
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