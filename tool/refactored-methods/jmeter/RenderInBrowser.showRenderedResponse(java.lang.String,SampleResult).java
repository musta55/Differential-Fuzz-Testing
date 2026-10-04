protected void showRenderedResponse(String response, SampleResult res) {
    if (response == null) {
        results.setText("");
        return;
    }
    String html = extractHtmlContent(response);
    if (browserPanel == null) {
        browserPanel = initComponents(html);
    }
    browserPanel.setVisible(true);
    resultsScrollPane.setViewportView(browserPanel);
    Platform.runLater(() -> engine.loadContent(html));
}
// ---- helper method(s) introduced by the refactoring ----
private String extractHtmlContent(String response) {
    int htmlIndex = response.indexOf("<HTML");
    if (htmlIndex < 0) {
        htmlIndex = response.indexOf("<html");
    }
    if (htmlIndex < 0) {
        htmlIndex = 0;
    }
    return response.substring(htmlIndex);
}

