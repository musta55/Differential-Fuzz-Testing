private JPanel makeParameterPanel() {
    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.add(createScopePanel(true, true, true), BorderLayout.NORTH);
    JPanel panel = new JPanel(new MigLayout("fillx, wrap 2, insets 0", "[][fill,grow]"));
    panel.setBorder(BorderFactory.createTitledBorder(JMeterUtils.getResString("xpath2_extractor_properties")));
    panel.add(JMeterUtils.labelFor(refNameField, "ref_name_field"));
    panel.add(refNameField);
    panel.add(JMeterUtils.labelFor(xpathQueryField, "xpath_extractor_query"));
    panel.add(xpathQueryField);
    panel.add(JMeterUtils.labelFor(matchNumberField, "match_num_field"));
    panel.add(matchNumberField);
    panel.add(JMeterUtils.labelFor(defaultField, "default_value_field"));
    panel.add(defaultField);
    namespacesTA = JSyntaxTextArea.getInstance(5, 80);
    JTextScrollPane namespaceJSP = JTextScrollPane.getInstance(namespacesTA, true);
    panel.add(JMeterUtils.labelFor(namespaceJSP, "xpath_extractor_user_namespaces"));
    panel.add(namespaceJSP);
    //$NON-NLS-1$
    getFragment = new JCheckBox(JMeterUtils.getResString("xpath_extractor_fragment"));
    panel.add(getFragment, "span 2");
    mainPanel.add(panel, BorderLayout.CENTER);
    return mainPanel;
}