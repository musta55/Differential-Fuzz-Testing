/**
 * request pane content
 */
/* (non-Javadoc)
     * @see org.apache.jmeter.visualizers.request.RequestView#init()
     */
@Override
public void init() {
    paneRaw = new JPanel(new BorderLayout(0, 5));
    sampleDataField = JSyntaxTextArea.getInstance(20, 80, true);
    sampleDataField.setEditable(false);
    sampleDataField.setLineWrap(true);
    sampleDataField.setWrapStyleWord(true);
    JPanel requestAndSearchPanel = new JPanel(new BorderLayout());
    requestAndSearchPanel.add(new JSyntaxSearchToolBar(sampleDataField).getToolBar(), BorderLayout.NORTH);
    requestAndSearchPanel.add(JTextScrollPane.getInstance(sampleDataField), BorderLayout.CENTER);
    headerData = JSyntaxTextArea.getInstance(20, 80, true);
    headerData.setEditable(false);
    headerData.setLineWrap(true);
    headerData.setWrapStyleWord(true);
    JPanel headerAndSearchPanel = new JPanel(new BorderLayout());
    headerAndSearchPanel.add(new JSyntaxSearchToolBar(headerData).getToolBar(), BorderLayout.NORTH);
    headerAndSearchPanel.add(JTextScrollPane.getInstance(headerData), BorderLayout.CENTER);
    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    tabbedPane.addTab(JMeterUtils.getResString("view_results_request_body"), new JScrollPane(requestAndSearchPanel));
    tabbedPane.addTab(JMeterUtils.getResString("view_results_request_headers"), new JScrollPane(headerAndSearchPanel));
    paneRaw.add(GuiUtils.makeScrollPane(tabbedPane));
}