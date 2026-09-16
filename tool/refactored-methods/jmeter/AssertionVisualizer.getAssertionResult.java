private static String getAssertionResult(SampleResult res) {
    if (res == null) {
        return "";
    }
    StringBuilder display = new StringBuilder();
    for (AssertionResult item : res.getAssertionResults()) {
        if (item.isFailure() || item.isError()) {
            appendFailureMessage(display, item);
        }
    }
    return display.toString();
}
// ---- helper method(s) introduced by the refactoring ----
private static void appendFailureMessage(StringBuilder display, AssertionResult item) {
    // $NON-NLS-1$
    display.append("\n\t");
    // $NON-NLS-1$
    display.append(item.getName() != null ? item.getName() + " : " : "");
    display.append(item.getFailureMessage());
}

private void setupMainPanel() {
    Border margin = new EmptyBorder(10, 10, 5, 10);
    this.setBorder(margin);
    this.add(makeTitlePanel(), BorderLayout.NORTH);
}

private void setupTextArea() {
    // $NON-NLS-1$
    JLabel textAreaLabel = new JLabel(JMeterUtils.getResString("assertion_textarea_label"));
    textAreaLabel.setLabelFor(textArea);
    Box mainPanel = Box.createVerticalBox();
    mainPanel.add(textAreaLabel);
    textArea = new JTextArea();
    textArea.setEditable(false);
    textArea.setLineWrap(false);
    JScrollPane areaScrollPane = new JScrollPane(textArea);
    areaScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    areaScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    areaScrollPane.setPreferredSize(new Dimension(mainPanel.getWidth(), mainPanel.getHeight()));
    mainPanel.add(areaScrollPane);
    this.add(mainPanel, BorderLayout.CENTER);
}

