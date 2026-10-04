protected void showRenderedResponse(String response, SampleResult res) {
    if (response == null) {
        results.setText("");
        return;
    }
    // could be <HTML lang=""> //
    int htmlIndex = response.indexOf("<HTML");
    // $NON-NLS-1$
    // Look for a case variation
    if (htmlIndex < 0) {
        // ditto // $NON-NLS-1$
        htmlIndex = response.indexOf("<html");
    }
    // If we still can't find it, just try using all of the text
    if (htmlIndex < 0) {
        htmlIndex = 0;
    }
    final String html = response.substring(htmlIndex);
    if (browserPanel == null) {
        browserPanel = initComponents(html);
    }
    browserPanel.setVisible(true);
    resultsScrollPane.setViewportView(browserPanel);
    Platform.runLater(() -> engine.loadContent(html));
}