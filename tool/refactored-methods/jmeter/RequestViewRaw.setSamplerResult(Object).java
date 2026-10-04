/* (non-Javadoc)
     * @see org.apache.jmeter.visualizers.request.RequestView#setSamplerResult(java.lang.Object)
     */
@Override
public void setSamplerResult(Object objectResult) {
    if (objectResult instanceof SampleResult) {
        SampleResult sampleResult = (SampleResult) objectResult;
        updateHeaderData(sampleResult);
        updateSampleDataField(sampleResult);
    }
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

