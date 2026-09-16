/**
 * request pane content
 */
/* (non-Javadoc)
     * @see org.apache.jmeter.visualizers.request.RequestView#init()
     */
@Override
public void init() {
    paneRaw = new JPanel(new BorderLayout(0, 5));
    sampleDataField = createJSyntaxTextArea();
    JPanel requestAndSearchPanel = createPanelWithSearchToolBar(sampleDataField);
    headerData = createJSyntaxTextArea();
    JPanel headerAndSearchPanel = createPanelWithSearchToolBar(headerData);
    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    tabbedPane.addTab(JMeterUtils.getResString("view_results_request_body"), new JScrollPane(requestAndSearchPanel));
    tabbedPane.addTab(JMeterUtils.getResString("view_results_request_headers"), new JScrollPane(headerAndSearchPanel));
    paneRaw.add(GuiUtils.makeScrollPane(tabbedPane));
}
// ---- helper method(s) introduced by the refactoring ----
private static JSyntaxTextArea createJSyntaxTextArea() {
    JSyntaxTextArea textArea = JSyntaxTextArea.getInstance(20, 80, true);
    textArea.setEditable(false);
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    return textArea;
}

private static JPanel createPanelWithSearchToolBar(JSyntaxTextArea textArea) {
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(new JSyntaxSearchToolBar(textArea).getToolBar(), BorderLayout.NORTH);
    panel.add(JTextScrollPane.getInstance(textArea), BorderLayout.CENTER);
    return panel;
}

private void updateHeaderData(SampleResult sampleResult) {
    String rh = sampleResult.getRequestHeaders();
    if (rh != null && !rh.isEmpty()) {
        headerData.setInitialText(rh);
        sampleDataField.setCaretPosition(0);
    }
}

private void updateSampleDataField(SampleResult sampleResult) {
    String data = sampleResult.getSamplerData();
    if (data != null && !data.isEmpty()) {
        sampleDataField.setText(data);
        sampleDataField.setCaretPosition(0);
    } else {
        //$NON-NLS-1$
        sampleDataField.setText(JMeterUtils.getResString("view_results_table_request_raw_nodata"));
    }
}

