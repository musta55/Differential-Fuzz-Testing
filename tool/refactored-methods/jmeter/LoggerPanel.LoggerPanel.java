/**
 * Pane for display JMeter log file
 */
public LoggerPanel() {
    if (LOGGER_PANEL_MAX_LINES > 0) {
        events = new CircularFifoQueue<>(LOGGER_PANEL_MAX_LINES);
    } else {
        events = new ArrayDeque<>();
    }
    textArea = createAndConfigureTextArea();
    JScrollPane scrollPane = createScrollPane(textArea);
    add(scrollPane, BorderLayout.CENTER);
    initWorker();
}
// ---- helper method(s) introduced by the refactoring ----
private static JTextArea createAndConfigureTextArea() {
    JTextArea jTextArea;
    if (JMeterUtils.getPropDefault("loggerpanel.usejsyntaxtext", true)) {
        // JSyntax Text Area
        JSyntaxTextArea jSyntaxTextArea = JSyntaxTextArea.getInstance(15, 80, true);
        jSyntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
        jSyntaxTextArea.setCodeFoldingEnabled(false);
        jSyntaxTextArea.setAntiAliasingEnabled(false);
        jSyntaxTextArea.setEditable(false);
        jSyntaxTextArea.setLineWrap(false);
        jSyntaxTextArea.setLanguage("text");
        // space between borders and text
        jSyntaxTextArea.setMargin(new Insets(2, 2, 2, 2));
        jTextArea = jSyntaxTextArea;
    } else {
        // Plain text area
        jTextArea = new JTextArea(15, 80);
    }
    return jTextArea;
}

private static JScrollPane createScrollPane(JTextArea textArea) {
    JScrollPane areaScrollPane = new JScrollPane(textArea);
    areaScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    areaScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    return areaScrollPane;
}

private String buildLogText() {
    StringBuilder builder = new StringBuilder();
    synchronized (events) {
        for (String line : events) {
            builder.append(line);
        }
    }
    return builder.toString();
}

private void updateTextArea(String logText) {
    if (LOGGER_PANEL_MAX_LINES > 0) {
        textArea.setText(logText);
    } else {
        textArea.append(logText);
    }
    textArea.setCaretPosition(textArea.getText().length());
}

