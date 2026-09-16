private JPanel createSignerPanel() {
    JPanel panel = new JPanel();
    panel.setBorder(BorderFactory.createTitledBorder(JMeterUtils.getResString(// $NON-NLS-1$
    "smime_assertion_signer")));
    panel.setLayout(new VerticalLayout(5, VerticalLayout.LEFT));
    ButtonGroup buttonGroup = new ButtonGroup();
    buttonGroup.add(signerNoCheck);
    buttonGroup.add(signerCheckConstraints);
    buttonGroup.add(signerCheckByFile);
    panel.add(signerNoCheck);
    panel.add(signerCheckConstraints);
    signerCheckConstraints.addChangeListener(evt -> {
        boolean signerCC = signerCheckConstraints.isSelected();
        enableSignerFields(signerCC);
        issuerDnField.setEnabled(signerCC);
    });
    panel.add(createLabeledTextField("smime_assertion_signer_dn", signerDnField));
    panel.add(createLabeledTextField("smime_assertion_signer_email", signerEmailField));
    panel.add(createLabeledTextField("smime_assertion_issuer_dn", issuerDnField));
    panel.add(createLabeledTextField("smime_assertion_signer_serial", signerSerialNumberField));
    signerCheckByFile.addChangeListener(evt -> signerCertFile.setEnabled(signerCheckByFile.isSelected()));
    panel.add(createLabeledTextField("smime_assertion_signer_cert_file", signerCertFile));
    return panel;
}
// ---- helper method(s) introduced by the refactoring ----
private static JPanel createLabeledTextField(String labelKey, JTextField textField) {
    JPanel panel = new JPanel();
    // $NON-NLS-1$
    panel.add(new JLabel(JMeterUtils.getResString(labelKey)));
    panel.add(Box.createHorizontalStrut(5));
    panel.add(textField);
    return panel;
}

private void enableSignerFields(boolean enabled) {
    signerDnField.setEnabled(enabled);
    signerSerialNumberField.setEnabled(enabled);
    signerEmailField.setEnabled(enabled);
    issuerDnField.setEnabled(enabled);
}

