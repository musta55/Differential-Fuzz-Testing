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
        signerDnField.setEnabled(signerCC);
        signerSerialNumberField.setEnabled(signerCC);
        signerEmailField.setEnabled(signerCC);
        issuerDnField.setEnabled(signerCC);
    });
    Box box = Box.createHorizontalBox();
    // $NON-NLS-1$
    box.add(new JLabel(JMeterUtils.getResString("smime_assertion_signer_dn")));
    box.add(Box.createHorizontalStrut(5));
    box.add(signerDnField);
    panel.add(box);
    box = Box.createHorizontalBox();
    // $NON-NLS-1$
    box.add(new JLabel(JMeterUtils.getResString("smime_assertion_signer_email")));
    box.add(Box.createHorizontalStrut(5));
    box.add(signerEmailField);
    panel.add(box);
    box = Box.createHorizontalBox();
    // $NON-NLS-1$
    box.add(new JLabel(JMeterUtils.getResString("smime_assertion_issuer_dn")));
    box.add(Box.createHorizontalStrut(5));
    box.add(issuerDnField);
    panel.add(box);
    box = Box.createHorizontalBox();
    // $NON-NLS-1$
    box.add(new JLabel(JMeterUtils.getResString("smime_assertion_signer_serial")));
    box.add(Box.createHorizontalStrut(5));
    box.add(signerSerialNumberField);
    panel.add(box);
    signerCheckByFile.addChangeListener(evt -> signerCertFile.setEnabled(signerCheckByFile.isSelected()));
    box = Box.createHorizontalBox();
    box.add(signerCheckByFile);
    box.add(Box.createHorizontalStrut(5));
    box.add(signerCertFile);
    panel.add(box);
    return panel;
}