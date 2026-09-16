private JPanel makeParameterPanel() {
    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.add(createScopePanel(true, true, true), BorderLayout.NORTH);
    mainPanel.add(createPropertiesPanel(), BorderLayout.CENTER);
    return mainPanel;
}
// ---- helper method(s) introduced by the refactoring ----
private JPanel createPropertiesPanel() {
    JPanel panel = new JPanel(new MigLayout("fillx, wrap 2, insets 0", "[][fill,grow]"));
    panel.setBorder(BorderFactory.createTitledBorder(JMeterUtils.getResString("xpath2_extractor_properties")));
    addRefNameField(panel);
    addXPathQueryField(panel);
    addMatchNumberField(panel);
    addDefaultField(panel);
    addNamespacesField(panel);
    addGetFragmentCheckBox(panel);
    return panel;
}

private void addRefNameField(JPanel panel) {
    panel.add(JMeterUtils.labelFor(refNameField, "ref_name_field"));
    panel.add(refNameField);
}

private void addXPathQueryField(JPanel panel) {
    panel.add(JMeterUtils.labelFor(xpathQueryField, "xpath_extractor_query"));
    panel.add(xpathQueryField);
}

private void addMatchNumberField(JPanel panel) {
    panel.add(JMeterUtils.labelFor(matchNumberField, "match_num_field"));
    panel.add(matchNumberField);
}

private void addDefaultField(JPanel panel) {
    panel.add(JMeterUtils.labelFor(defaultField, "default_value_field"));
    panel.add(defaultField);
}

private void addNamespacesField(JPanel panel) {
    namespacesTA = JSyntaxTextArea.getInstance(5, 80);
    JTextScrollPane namespaceJSP = JTextScrollPane.getInstance(namespacesTA, true);
    panel.add(JMeterUtils.labelFor(namespaceJSP, "xpath_extractor_user_namespaces"));
    panel.add(namespaceJSP);
}

private void addGetFragmentCheckBox(JPanel panel) {
    //$NON-NLS-1$
    getFragment = new JCheckBox(JMeterUtils.getResString("xpath_extractor_fragment"));
    panel.add(getFragment, "span 2");
}

